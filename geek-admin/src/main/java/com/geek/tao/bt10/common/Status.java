package com.geek.tao.bt10.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.geek.common.constant.CacheConstants;
import com.geek.common.utils.CacheUtils;

import lombok.Getter;

/**
 * bt10 业务状态枚举（使用项目通用 CacheUtils）
 */
public class Status {

    /**
     * 自动扫描本类中的所有枚举内类，按 code->desc 构建并缓存。
     * 使用项目通用缓存 CacheUtils，缓存名见 {@link CacheConstants#BT10_STATUS_KEY}
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Map<String, String>> getStatusCache() {
        Map<String, Map<String, String>> result = new HashMap<>();
        Class<?>[] innerClasses = Status.class.getDeclaredClasses();
        for (Class<?> inner : innerClasses) {
            if (!inner.isEnum()) {
                continue;
            }
            String cacheKey = inner.getName();
            Map<String, String> cached = CacheUtils.get(CacheConstants.BT10_STATUS_KEY, cacheKey, Map.class);
            if (cached == null) {
                Map<String, String> map = new LinkedHashMap<>();
                try {
                    Method getCode = inner.getMethod("getCode");
                    Method getDesc = inner.getMethod("getDesc");
                    Object[] constants = inner.getEnumConstants();
                    if (constants != null) {
                        for (Object constant : constants) {
                            String code = String.valueOf(getCode.invoke(constant));
                            String desc = String.valueOf(getDesc.invoke(constant));
                            map.put(code, desc);
                        }
                    }
                } catch (ReflectiveOperationException e) {
                    // 如果某个枚举不符合约定（没有 getCode/getDesc），跳过该枚举
                    continue;
                }
                CacheUtils.put(CacheConstants.BT10_STATUS_KEY, cacheKey, map);
                cached = map;
            }
            result.put(cacheKey, cached);
        }
        return result;
    }

    @Getter
    enum Event {
        DRAFT("DRAFT", "草稿")
        , PUBLISHED("PUBLISHED", "已发布")
        , REGISTRATION("REGISTRATION", "报名中")
        , FULL("FULL", "已满")
        , ONGOING("ONGOING", "进行中")
        , ENDED("ENDED", "已结束")
        , CANCELLED("CANCELLED", "已取消")
        ;

        private final String code;
        private final String desc;

        Event(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

}
