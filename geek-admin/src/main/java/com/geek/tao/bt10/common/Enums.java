package com.geek.tao.bt10.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.geek.common.constant.CacheConstants;
import com.geek.common.utils.CacheUtils;

import lombok.Getter;

/**
 * bt10 枚举与缓存（使用项目通用 CacheUtils）
 */
public class Enums {
    
    static {
        getEnumsCache();
    }

    /**
     * 自动扫描本类中的所有枚举内类，按 code->desc 构建并缓存。
     * 使用项目通用缓存 CacheUtils，缓存名见 {@link CacheConstants#BT10_ENUM_KEY}
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Map<String, String>> getEnumsCache() {
        Map<String, Map<String, String>> result = new HashMap<>();
        Class<?>[] innerClasses = Enums.class.getDeclaredClasses();
        for (Class<?> inner : innerClasses) {
            if (!inner.isEnum()) {
                continue;
            }
            String cacheKey = inner.getName();
            Map<String, String> cached = CacheUtils.get(CacheConstants.BT10_ENUM_KEY, cacheKey, Map.class);
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
                CacheUtils.put(CacheConstants.BT10_ENUM_KEY, cacheKey, map);
                cached = map;
            }
            result.put(cacheKey, cached);
        }
        return result;
    }

    /**
     * 通用-状态
     */
    @Getter
    enum Status {
        /** 正常 */
        OK("0", "正常")
        /** 停用 */
        , DISABLE("1", "停用")
        /** 删除 */
        , DELETED("2", "删除")
        ;
        private final String code;
        private final String desc;
        Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 通用-是否
     */
    @Getter
    enum Yes {
        /** 是 */
        YES("0", "是")
        /** 否 */
        , NO("1", "否")
        ;
        private final String code;
        private final String desc;
        Yes(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 通用-性别
     */
    @Getter
    enum Sex {
        /** 男 */
        MALE("0", "男")
        /** 女 */
        , FEMALE("1", "女")
        /** 未知 */
        , UNKNOWN("2", "未知")
        ;
        private final String code;
        private final String desc;
        Sex(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 活动-活动类型
     */
    @Getter
    enum EventType {
        WORKSHOP("WORKSHOP", "工作坊")
        , COURSE("COURSE", "课程")
        , MEETUP("MEETUP", "见面会")
        , LIVE("LIVE", "直播")
        , OTHER("OTHER", "其他")
        ;
        private final String code;
        private final String desc;
        EventType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 活动-参与方式
     */
    @Getter
    enum EventJoinType {
        ONLINE("ONLINE", "线上")
        , OFFLINE("OFFLINE", "线下")
        , MIXED("MIXED", "混合")
        ;
        private final String code;
        private final String desc;
        EventJoinType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 标签-标签类型
     */
    @Getter
    enum TagType {
        ONLINE("ONLINE", "身份")
        , OFFLINE("OFFLINE", "知识点")
        , ALL("ALL", "活动名称")
        , OTHER("OTHER", "其他")
        ;
        private final String code;
        private final String desc;
        TagType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
