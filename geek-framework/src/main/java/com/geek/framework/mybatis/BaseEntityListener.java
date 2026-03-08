package com.geek.framework.mybatis;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import com.geek.common.core.domain.BaseEntity;
import com.geek.common.utils.DateUtils;
import com.geek.common.utils.SecurityUtils;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

/**
 * 实体插入/更新监听：对 createTime/createBy/createId、updateTime/updateBy/updateId 等字段
 * 仅当当前值为空时自动填充，不覆盖调用方已设置的值。
 * 通过反射识别实例上是否存在对应 getter/setter，子类上的 createId/updateId 也会被处理。
 * <p>
 * 按实体 Class 缓存 getter/setter，高并发下仅首访做反射解析，后续直接调用缓存 Method。
 * </p>
 */
public class BaseEntityListener implements InsertListener, UpdateListener {

    private static final ConcurrentHashMap<Class<?>, FillSlots> INSERT_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, FillSlots> UPDATE_CACHE = new ConcurrentHashMap<>();

    @Override
    public void onUpdate(Object entity) {
        if (!(entity instanceof BaseEntity)) {
            return;
        }
        FillSlots slots = UPDATE_CACHE.computeIfAbsent(entity.getClass(), BaseEntityListener::resolveUpdateSlots);
        boolean anonymous = SecurityUtils.isAnonymous();
        String updateBy = anonymous ? null : SecurityUtils.getUsername();
        Long updateId = anonymous ? null : SecurityUtils.getUserId();

        slots.fillIfEmpty(entity, DateUtils.getNowInstant(), updateBy, updateId);
    }

    @Override
    public void onInsert(Object entity) {
        if (!(entity instanceof BaseEntity)) {
            return;
        }
        FillSlots slots = INSERT_CACHE.computeIfAbsent(entity.getClass(), BaseEntityListener::resolveInsertSlots);
        boolean anonymous = SecurityUtils.isAnonymous();
        String createBy = anonymous ? null : SecurityUtils.getUsername();
        Long createId = anonymous ? null : SecurityUtils.getUserId();

        slots.fillIfEmpty(entity, DateUtils.getNowInstant(), createBy, createId);
    }

    /** 插入用：createTime, createBy, createId */
    private static FillSlots resolveInsertSlots(Class<?> clazz) {
        FillSlots s = new FillSlots();
        s.id = slot(clazz, "createId", Long.class);
        s.by = slot(clazz, "createBy", String.class);
        s.time = slot(clazz, "createTime", Instant.class);
        return s;
    }

    /** 更新用：updateTime, updateBy, updateId */
    private static FillSlots resolveUpdateSlots(Class<?> clazz) {
        FillSlots s = new FillSlots();
        s.id = slot(clazz, "updateId", Long.class);
        s.by = slot(clazz, "updateBy", String.class);
        s.time = slot(clazz, "updateTime", Instant.class);
        return s;
    }

    private static Slot slot(Class<?> clazz, String property, Class<?> valueType) {
        Method getter = findMethod(clazz, "get", property);
        Method setter = findMethod(clazz, "set", property);
        if (getter == null || setter == null || setter.getParameterTypes().length != 1) {
            return null;
        }
        Class<?> setterParam = setter.getParameterTypes()[0];
        getter.setAccessible(true);
        setter.setAccessible(true);
        Slot slot = new Slot();
        slot.getter = getter;
        slot.setter = setter;
        slot.setterParam = setterParam;
        slot.valueType = valueType;
        return slot;
    }

    private static Method findMethod(Class<?> clazz, String prefix, String propertyName) {
        if (propertyName == null || propertyName.isEmpty()) {
            return null;
        }
        String cap = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        String methodName = prefix + cap;
        int paramCount = "set".equals(prefix) ? 1 : 0;
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == paramCount) {
                    return m;
                }
            }
        }
        return null;
    }

    private static boolean isEmpty(Object v) {
        if (v == null) {
            return true;
        }
        if (v instanceof String) {
            return ((String) v).isEmpty();
        }
        return false;
    }

    /** 单个属性的 getter/setter 及类型，用于按值类型转换后写入 */
    private static final class Slot {
        Method getter;
        Method setter;
        Class<?> setterParam;
        Class<?> valueType;

        void fillIfEmpty(Object entity, Object value) {
            if (value == null && (setterParam == long.class || setterParam == Long.class)) {
                return;
            }
            try {
                Object current = getter.invoke(entity);
                if (!isEmpty(current)) {
                    return;
                }
                Object toSet = value;
                if (setterParam == String.class && value instanceof Long) {
                    toSet = String.valueOf(value);
                } else if (value != null && setterParam != valueType && setterParam != String.class) {
                    return;
                }
                setter.invoke(entity, toSet);
            } catch (ReflectiveOperationException ignored) {
                // 类型不兼容等跳过
            }
        }
    }

    /** 一组三个槽位：时间、人名字符串、人ID */
    private static final class FillSlots {
        Slot time;
        Slot by;
        Slot id;

        void fillIfEmpty(Object entity, Instant now, String byValue, Long idValue) {
            if (time != null) {
                time.fillIfEmpty(entity, now);
            }
            if (by != null) {
                by.fillIfEmpty(entity, byValue);
            }
            if (id != null) {
                id.fillIfEmpty(entity, idValue);
            }
        }
    }
}
