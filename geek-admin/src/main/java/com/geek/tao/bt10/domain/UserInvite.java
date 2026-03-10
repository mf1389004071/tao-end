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
 * 邀请关系与奖励记录对象 user_invite
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_invite")
@Schema(title = "邀请关系与奖励记录对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInvite extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 被邀请人用户ID(sys_user.user_id) */
    @Schema(title = "被邀请人用户ID(sys_user.user_id)")
    @Excel(name = "被邀请人用户ID(sys_user.user_id)")
    private Long userId;

    /** 邀请人用户ID */
    @Schema(title = "邀请人用户ID")
    @Excel(name = "邀请人用户ID")
    private Long inviterId;

    /** 使用的邀请码 */
    @Schema(title = "使用的邀请码")
    @Excel(name = "使用的邀请码")
    private String inviteCode;

    /** 被邀请时间 */
    @Schema(title = "被邀请时间")
    @Excel(name = "被邀请时间")
    private Instant inviteTime;

    /** 状态：待处理/已接受/已发奖 */
    @Schema(title = "状态：待处理/已接受/已发奖")
    @Excel(name = "状态：待处理/已接受/已发奖")
    private String rewardStatus;

    /** 邀请人是否已领取奖励 */
    @Schema(title = "邀请人是否已领取奖励")
    @Excel(name = "邀请人是否已领取奖励")
    private Boolean rewardClaimed;

    /** 邀请奖励积分 */
    @Schema(title = "邀请奖励积分")
    @Excel(name = "邀请奖励积分")
    private Integer rewardPoints;

    /** 邀请奖励贡献点 */
    @Schema(title = "邀请奖励贡献点")
    @Excel(name = "邀请奖励贡献点")
    private BigDecimal rewardContrib;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
