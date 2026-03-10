package com.geek.tao.bt10.domain;

import java.math.BigDecimal;
import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动所需角色对象 event_role
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_role")
@Schema(title = "活动所需角色对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventRole extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 活动ID */
    @Schema(title = "活动ID")
    @Excel(name = "活动ID")
    private Long eventId;

    /** 角色名：天使/主持人/PM/助教/签到/主讲等 */
    @Schema(title = "角色名：天使/主持人/PM/助教/签到/主讲等")
    @Excel(name = "角色名：天使/主持人/PM/助教/签到/主讲等")
    private String roleName;

    /** 角色说明 */
    @Schema(title = "角色说明")
    @Excel(name = "角色说明")
    private String roleDescription;

    /** 该角色最大人数 */
    @Schema(title = "该角色最大人数")
    @Excel(name = "该角色最大人数")
    private Integer maxParticipants;

    /** 当前已分配人数 */
    @Schema(title = "当前已分配人数")
    @Excel(name = "当前已分配人数")
    private Integer currentParticipants;

    /** 角色权限配置 */
    @Schema(title = "角色权限配置")
    @Excel(name = "角色权限配置")
    private String permissions;

    /** 担任该角色奖励积分 */
    @Schema(title = "担任该角色奖励积分")
    @Excel(name = "担任该角色奖励积分")
    private Integer pointsReward;

    /** 担任该角色奖励贡献点 */
    @Schema(title = "担任该角色奖励贡献点")
    @Excel(name = "担任该角色奖励贡献点")
    private BigDecimal contribReward;

    /** 职责说明 */
    @Schema(title = "职责说明")
    @Excel(name = "职责说明")
    private String responsibilities;

    /** 任职要求 */
    @Schema(title = "任职要求")
    @Excel(name = "任职要求")
    private String requirements;

    /** 排序 */
    @Schema(title = "排序")
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
