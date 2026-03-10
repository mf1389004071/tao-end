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
 * 报名记录与活动角色的分配关系对象 event_joiner_role
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_joiner_role")
@Schema(title = "报名记录与活动角色的分配关系对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventJoinerRole extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 报名记录ID */
    @Schema(title = "报名记录ID")
    @Excel(name = "报名记录ID")
    private Long joinId;

    /** 活动角色ID */
    @Schema(title = "活动角色ID")
    @Excel(name = "活动角色ID")
    private Long roleId;

    /** 分配时间 */
    @Schema(title = "分配时间")
    @Excel(name = "分配时间")
    private Instant assignedTime;

    /** 确认时间 */
    @Schema(title = "确认时间")
    @Excel(name = "确认时间")
    private Instant confirmedTime;

    /** 状态：已分配/已确认/已拒绝 */
    @Schema(title = "状态：已分配/已确认/已拒绝")
    @Excel(name = "状态：已分配/已确认/已拒绝")
    private String bizStatus;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
