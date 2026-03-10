package com.geek.tao.bt10.domain;

import java.math.BigDecimal;
import java.time.Instant;
import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息画像扩展表对象 user_profiles
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_profiles")
@Schema(title = "用户信息画像扩展表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfiles extends BaseEntity
{


    /** 用户ID(主键且外键) */
    @Schema(title = "用户ID(主键且外键)")
    @Id
    private Long userId;

    /** 真实姓名 */
    @Schema(title = "真实姓名")
    @Excel(name = "真实姓名")
    private String realName;

    /** 当前积分余额 */
    @Schema(title = "当前积分余额")
    @Excel(name = "当前积分余额")
    private Integer points;

    /** 历史累计获得积分 */
    @Schema(title = "历史累计获得积分")
    @Excel(name = "历史累计获得积分")
    private Integer totalPoints;

    /** 贡献点余额(1:1人民币) */
    @Schema(title = "贡献点余额(1:1人民币)")
    @Excel(name = "贡献点余额(1:1人民币)")
    private BigDecimal contributionPoints;

    /** 历史累计贡献点 */
    @Schema(title = "历史累计贡献点")
    @Excel(name = "历史累计贡献点")
    private BigDecimal totalContributionPoints;

    /** 成长阶段：入门/学员/高手/导师 */
    @Schema(title = "成长阶段：入门/学员/高手/导师")
    @Excel(name = "成长阶段：入门/学员/高手/导师")
    private String growthStage;

    /** 加入/注册日期 */
    @Schema(title = "加入/注册日期")
    @Excel(name = "加入/注册日期")
    private Instant joinDate;

    /** 最后活跃时间 */
    @Schema(title = "最后活跃时间")
    @Excel(name = "最后活跃时间")
    private Instant lastActiveAt;

    /** 微信开放平台 unionid */
    @Schema(title = "微信开放平台 unionid")
    @Excel(name = "微信开放平台 unionid")
    private String wechatUnionid;

    /** 微信开放平台 openid */
    @Schema(title = "微信开放平台 openid")
    @Excel(name = "微信开放平台 openid")
    private String wechatOpenid;

    /** 小程序 openid */
    @Schema(title = "小程序 openid")
    @Excel(name = "小程序 openid")
    private String miniappOpenid;

    /** 企业微信用户ID */
    @Schema(title = "企业微信用户ID")
    @Excel(name = "企业微信用户ID")
    private String workUserid;

    /** 小鹅通用户ID(同步) */
    @Schema(title = "小鹅通用户ID(同步)")
    @Excel(name = "小鹅通用户ID(同步)")
    private String xiaoeUserId;

    /** 小鹅通原始数据快照 */
    @Schema(title = "小鹅通原始数据快照")
    @Excel(name = "小鹅通原始数据快照")
    private String xiaoeData;

    /** 邀请人用户ID(sys_user.user_id) */
    @Schema(title = "邀请人用户ID(sys_user.user_id)")
    @Excel(name = "邀请人用户ID(sys_user.user_id)")
    private Long inviterId;

    /** 本人邀请码，用于邀请好友 */
    @Schema(title = "本人邀请码，用于邀请好友")
    @Excel(name = "本人邀请码，用于邀请好友")
    private String invitationCode;

    /** 业务角色：创始人/联创/合伙人/高手/城市主理人/会员 */
    @Schema(title = "业务角色：创始人/联创/合伙人/高手/城市主理人/会员")
    @Excel(name = "业务角色：创始人/联创/合伙人/高手/城市主理人/会员")
    private String bizRole;

    /** 最美照片URL */
    @Schema(title = "最美照片URL")
    @Excel(name = "最美照片URL")
    private String bestPhotoUrl;

    /** 微信头像URL(同步用) */
    @Schema(title = "微信头像URL(同步用)")
    @Excel(name = "微信头像URL(同步用)")
    private String avatarWechatUrl;

    /** 宣传用头像URL */
    @Schema(title = "宣传用头像URL")
    @Excel(name = "宣传用头像URL")
    private String avatarPromoUrl;

    /** 个人宣传图URL */
    @Schema(title = "个人宣传图URL")
    @Excel(name = "个人宣传图URL")
    private String promoImageUrl;

    /** 心树图URL(商业定位成果) */
    @Schema(title = "心树图URL(商业定位成果)")
    @Excel(name = "心树图URL(商业定位成果)")
    private String heartTreeUrl;

    /** 心钥图URL(商业定位成果) */
    @Schema(title = "心钥图URL(商业定位成果)")
    @Excel(name = "心钥图URL(商业定位成果)")
    private String heartKeyUrl;

    /** 商业定位文案(探索本质) */
    @Schema(title = "商业定位文案(探索本质)")
    @Excel(name = "商业定位文案(探索本质)")
    private String businessPositioning;

    /** 天赋解读与能力总结 */
    @Schema(title = "天赋解读与能力总结")
    @Excel(name = "天赋解读与能力总结")
    private String talentSummary;

    /** 可交流时段描述 */
    @Schema(title = "可交流时段描述")
    @Excel(name = "可交流时段描述")
    private String availableTimeSlots;

    /** 可约状态：可约/已约/不约 */
    @Schema(title = "可约状态：可约/已约/不约")
    @Excel(name = "可约状态：可约/已约/不约")
    private String appointmentStatus;

    /** 额外画像信息(JSON)，如家庭成员/挑战列表等 */
    @Schema(title = "额外画像信息(JSON)，如家庭成员/挑战列表等")
    @Excel(name = "额外画像信息(JSON)，如家庭成员/挑战列表等")
    private String extraProfile;

    /** 扩展文本1 */
    @Schema(title = "扩展文本1")
    @Excel(name = "扩展文本1")
    private String text1;

    /** 扩展文本2 */
    @Schema(title = "扩展文本2")
    @Excel(name = "扩展文本2")
    private String text2;

    /** 扩展文本3 */
    @Schema(title = "扩展文本3")
    @Excel(name = "扩展文本3")
    private String text3;

    /** 扩展JSON */
    @Schema(title = "扩展JSON")
    @Excel(name = "扩展JSON")
    private String jsonData;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
