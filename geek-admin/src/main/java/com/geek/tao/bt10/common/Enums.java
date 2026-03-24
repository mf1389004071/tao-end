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

    /**
     * 订单支付方式：贡献点/积分/混合(现金+积分)/混合(现金+贡献点)/现金
     */
    @Getter
    public enum PaymentMethod {
        CONTRIBUTION("CONTRIBUTION", "贡献点")
        , POINTS("POINTS", "积分")
        , CASH_POINTS_MIXED("CASH_POINTS_MIXED", "混合(现金+积分)")
        , CASH_CONTRIBUTION_MIXED("CASH_CONTRIBUTION_MIXED", "混合(现金+贡献点)")
        , CASH("CASH", "现金")
        ;

        private final String code;
        private final String desc;

        PaymentMethod(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 签到方式：二维码/定位/手动
     */
    @Getter
    public enum CheckInMethod {
        QR_CODE("QR_CODE", "二维码")
        , LOCATION("LOCATION", "定位")
        , MANUAL("MANUAL", "手动")
        ;

        private final String code;
        private final String desc;

        CheckInMethod(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 通知类型
     */
    @Getter
    public enum NotificationType {
        SYSTEM("SYSTEM", "系统通知")
        , INTERACTION("INTERACTION", "互动通知")
        , SUBSCRIPTION("SUBSCRIPTION", "订阅通知")
        ;
        private final String code;
        private final String desc;
        NotificationType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 关联业务类型（通用）
     */
    @Getter
    public enum RelatedType {
        EVENT("EVENT", "活动")
        , CONTENT("CONTENT", "内容")
        , USER("USER", "用户")
        , ORDER("ORDER", "订单")
        , PRODUCT("PRODUCT", "商品")
        , IDENTITY("IDENTITY", "身份")
        ;
        private final String code;
        private final String desc;
        RelatedType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 活动日志事件类型
     */
    @Getter
    public enum ActivityEventType {
        LOGIN("LOGIN", "登录")
        , LOGOUT("LOGOUT", "退出")
        , SIGNIN("SIGNIN", "签到")
        , LIKE("LIKE", "点赞")
        , COMMENT("COMMENT", "评论")
        , SHARE("SHARE", "分享")
        , REGISTER_EVENT("REGISTER_EVENT", "活动报名")
        ;
        private final String code;
        private final String desc;
        ActivityEventType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 活动日志事件来源类型
     */
    @Getter
    public enum ActivityEventSourceType {
        EVENT("EVENT", "活动")
        , CONTENT("CONTENT", "内容")
        , USER("USER", "用户")
        ;
        private final String code;
        private final String desc;
        ActivityEventSourceType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 订单类型
     */
    @Getter
    public enum OrderType {
        EVENT("EVENT", "活动订单")
        , PRODUCT("PRODUCT", "商品订单")
        , RECHARGE("RECHARGE", "充值订单")
        ;
        private final String code;
        private final String desc;
        OrderType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 支付明细类型
     */
    @Getter
    public enum PaymentItemType {
        PRODUCT("PRODUCT", "商品")
        , SERVICE("SERVICE", "服务")
        , COUPON("COUPON", "券")
        , IDENTITY("IDENTITY", "身份")
        ;
        private final String code;
        private final String desc;
        PaymentItemType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 明细发放方式
     */
    @Getter
    public enum GrantMethod {
        AUTO("AUTO", "自动发放")
        , MANUAL("MANUAL", "手动发放")
        , COUPON("COUPON", "券核销")
        ;
        private final String code;
        private final String desc;
        GrantMethod(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 明细使用状态
     */
    @Getter
    public enum UsageStatus {
        UNUSED("UNUSED", "未使用")
        , USED("USED", "已使用")
        , EXPIRED("EXPIRED", "已过期")
        ;
        private final String code;
        private final String desc;
        UsageStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 知识内容类型
     */
    @Getter
    public enum KnowledgeContentType {
        ARTICLE("ARTICLE", "文章")
        , TOOL("TOOL", "工具")
        , CASE("CASE", "案例")
        , WIKI("WIKI", "Wiki")
        ;
        private final String code;
        private final String desc;
        KnowledgeContentType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * AI任务类型
     */
    @Getter
    public enum TaskType {
        TRANSCRIBE("TRANSCRIBE", "转写")
        , SUMMARY("SUMMARY", "摘要")
        , VECTORIZE("VECTORIZE", "向量化")
        , CHAT("CHAT", "对话")
        ;
        private final String code;
        private final String desc;
        TaskType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 行为类型（积分/贡献点流水）
     */
    @Getter
    public enum ActionType {
        SIGNIN("SIGNIN", "签到")
        , PUBLISH("PUBLISH", "发布内容")
        , INVITE("INVITE", "邀请")
        , RECHARGE("RECHARGE", "充值")
        , PURCHASE("PURCHASE", "购买")
        , REFUND("REFUND", "退款")
        , REWARD("REWARD", "奖励")
        ;
        private final String code;
        private final String desc;
        ActionType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 变更类型（积分流水）
     */
    @Getter
    public enum ChangeType {
        GAIN("GAIN", "获得")
        , CONSUME("CONSUME", "消耗")
        , EXPIRE("EXPIRE", "过期")
        , ADJUST("ADJUST", "调整")
        ;
        private final String code;
        private final String desc;
        ChangeType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 操作人类型（审计）
     */
    @Getter
    public enum OperatorType {
        USER("USER", "用户")
        , SYSTEM("SYSTEM", "系统")
        , ADMIN("ADMIN", "管理员")
        ;
        private final String code;
        private final String desc;
        OperatorType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 用户成长阶段
     */
    @Getter
    public enum GrowthStage {
        BEGINNER("入门", "入门")
        , STUDENT("学员", "学员")
        , EXPERT("高手", "高手")
        , MENTOR("导师", "导师")
        ;
        private final String code;
        private final String desc;
        GrowthStage(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 用户业务角色
     */
    @Getter
    public enum BizRole {
        FOUNDER("创始人", "创始人")
        , CO_FOUNDER("联创", "联创")
        , PARTNER("合伙人", "合伙人")
        , EXPERT("高手", "高手")
        , CITY_HOST("城市主理人", "城市主理人")
        , MEMBER("会员", "会员")
        ;
        private final String code;
        private final String desc;
        BizRole(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 用户标签来源
     */
    @Getter
    public enum UserTagSource {
        SYSTEM("SYSTEM", "系统")
        , SELF("SELF", "自评")
        , COACH("COACH", "教练")
        ;
        private final String code;
        private final String desc;
        UserTagSource(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 社群成员角色
     */
    @Getter
    public enum CommunityMemberRole {
        OWNER("OWNER", "群主")
        , ADMIN("ADMIN", "管理员")
        , MEMBER("MEMBER", "成员")
        ;
        private final String code;
        private final String desc;
        CommunityMemberRole(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 用户变更字段类型
     */
    @Getter
    public enum UserChangeField {
        ROLE("ROLE", "角色")
        , STATUS("STATUS", "状态")
        , POINTS("POINTS", "积分")
        ;
        private final String code;
        private final String desc;
        UserChangeField(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 活动角色名称
     */
    @Getter
    public enum EventRoleName {
        ANGEL("ANGEL", "天使")
        , HOST("HOST", "主持人")
        , PM("PM", "PM")
        , ASSISTANT("ASSISTANT", "助教")
        , CHECK_IN("CHECK_IN", "签到")
        , SPEAKER("SPEAKER", "主讲")
        ;
        private final String code;
        private final String desc;
        EventRoleName(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 身份名称
     */
    @Getter
    public enum IdentityName {
        FOUNDER("创始人", "创始人")
        , CO_FOUNDER("联创", "联创")
        , PARTNER("合伙人", "合伙人")
        , EXPERT("高手", "高手")
        , FAN("粉丝", "粉丝")
        , CITY_HOST("城市主理人", "城市主理人")
        , COOPERATOR("合作方", "合作方")
        ;
        private final String code;
        private final String desc;
        IdentityName(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


}
