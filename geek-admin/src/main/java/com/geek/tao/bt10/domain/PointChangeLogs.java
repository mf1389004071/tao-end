package com.geek.tao.bt10.domain;

import java.time.Instant;
import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分变动审计对象 point_change_logs
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("point_change_logs")
@Schema(title = "积分变动审计对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PointChangeLogs extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 类型：获得/消耗/过期/调整等 */
    @Schema(title = "类型：获得/消耗/过期/调整等")
    @Excel(name = "类型：获得/消耗/过期/调整等")
    private String changeType;

    /** 积分变动量 */
    @Schema(title = "积分变动量")
    @Excel(name = "积分变动量")
    private Integer pointsChange;

    /** 变动前余额 */
    @Schema(title = "变动前余额")
    @Excel(name = "变动前余额")
    private Integer balanceBefore;

    /** 变动后余额 */
    @Schema(title = "变动后余额")
    @Excel(name = "变动后余额")
    private Integer balanceAfter;

    /** 原因说明 */
    @Schema(title = "原因说明")
    @Excel(name = "原因说明")
    private String changeReason;

    /** 关联业务类型 */
    @Schema(title = "关联业务类型")
    @Excel(name = "关联业务类型")
    private String relatedType;

    /** 关联业务ID */
    @Schema(title = "关联业务ID")
    @Excel(name = "关联业务ID")
    private Long relatedId;

    /** 操作人ID */
    @Schema(title = "操作人ID")
    @Excel(name = "操作人ID")
    private Long operatorId;

    /** 操作人类型 */
    @Schema(title = "操作人类型")
    @Excel(name = "操作人类型")
    private String operatorType;

    /** 变更时间 */
    @Schema(title = "变更时间")
    @Excel(name = "变更时间")
    private Instant changedTime;

    /** 扩展信息 */
    @Schema(title = "扩展信息")
    @Excel(name = "扩展信息")
    private String metadata;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
