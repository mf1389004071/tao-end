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

    /**
     * 活动状态
     */
    @Getter
    public enum Event {
        DRAFT("DRAFT", "草稿")
        , PUBLISHED("PUBLISHED", "已发布")
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

    /**
     * 活动参与状态：已报名/已确认/已取消/已签到/缺席
     */
    @Getter
    public enum EventJoin {
        REGISTERED("REGISTERED", "已报名")
        , CONFIRMED("CONFIRMED", "已确认")
        , CANCELLED("CANCELLED", "已取消")
        , CHECKED_IN("CHECKED_IN", "已签到")
        , ABSENT("ABSENT", "缺席")
        ;

        private final String code;
        private final String desc;

        EventJoin(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 订单支付状态：待支付/已支付/已全额退款/已部分退款/支付失败/已取消
     */
    @Getter
    public enum Payment {
        PENDING("PENDING", "待支付")
        , PAID("PAID", "已支付")
        , FULL_REFUNDED("FULL_REFUNDED", "已全额退款")
        , PARTIAL_REFUNDED("PARTIAL_REFUNDED", "已部分退款")
        , FAILED("FAILED", "支付失败")
        , CANCELLED("CANCELLED", "已取消")
        ;

        private final String code;
        private final String desc;

        Payment(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 转化状态：无/有意向/跟进中/已转化
     */
    @Getter
    public enum Conversion {
        NONE("NONE", "无")
        , INTENT("INTENT", "有意向")
        , FOLLOWING("FOLLOWING", "跟进中")
        , CONVERTED("CONVERTED", "已转化")
        ;

        private final String code;
        private final String desc;

        Conversion(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

}
