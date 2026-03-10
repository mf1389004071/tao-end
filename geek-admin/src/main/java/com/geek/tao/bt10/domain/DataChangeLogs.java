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
 * 通用业务数据变更审计对象 data_change_logs
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("data_change_logs")
@Schema(title = "通用业务数据变更审计对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DataChangeLogs extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 被变更表名 */
    @Schema(title = "被变更表名")
    @Excel(name = "被变更表名")
    private String tableName;

    /** 被变更记录ID */
    @Schema(title = "被变更记录ID")
    @Excel(name = "被变更记录ID")
    private Long recordId;

    /** 变更类型：新增/更新/删除 */
    @Schema(title = "变更类型：新增/更新/删除")
    @Excel(name = "变更类型：新增/更新/删除")
    private String changeType;

    /** 变更字段(更新时) */
    @Schema(title = "变更字段(更新时)")
    @Excel(name = "变更字段(更新时)")
    private String fieldName;

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

    /** 操作人用户ID */
    @Schema(title = "操作人用户ID")
    @Excel(name = "操作人用户ID")
    private Long operatorId;

    /** 操作人类型：用户/系统/管理员 */
    @Schema(title = "操作人类型：用户/系统/管理员")
    @Excel(name = "操作人类型：用户/系统/管理员")
    private String operatorType;

    /** 操作IP */
    @Schema(title = "操作IP")
    @Excel(name = "操作IP")
    private String ipAddress;

    /** UA */
    @Schema(title = "UA")
    @Excel(name = "UA")
    private String userAgent;

    /** 变更时间 */
    @Schema(title = "变更时间")
    @Excel(name = "变更时间")
    private Instant changedTime;

    /** 扩展元数据 */
    @Schema(title = "扩展元数据")
    @Excel(name = "扩展元数据")
    private String metadata;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
