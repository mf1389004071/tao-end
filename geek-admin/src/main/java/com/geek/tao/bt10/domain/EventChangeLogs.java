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
 * 活动关键信息变更记录对象 event_change_logs
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_change_logs")
@Schema(title = "活动关键信息变更记录对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventChangeLogs extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 活动ID */
    @Schema(title = "活动ID")
    @Excel(name = "活动ID")
    private Long eventId;

    /** 变更字段名 */
    @Schema(title = "变更字段名")
    @Excel(name = "变更字段名")
    private String changeField;

    /** 旧值 */
    @Schema(title = "旧值")
    @Excel(name = "旧值")
    private String oldValue;

    /** 新值 */
    @Schema(title = "新值")
    @Excel(name = "新值")
    private String newValue;

    /** 变更原因 */
    @Schema(title = "变更原因")
    @Excel(name = "变更原因")
    private String changeReason;

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
}
