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
 * AI异步任务对象 ai_tasks
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("ai_tasks")
@Schema(title = "AI异步任务对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class AiTasks extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 任务类型：转写/摘要/向量化/对话等 */
    @Schema(title = "任务类型：转写/摘要/向量化/对话等")
    @Excel(name = "任务类型：转写/摘要/向量化/对话等")
    private String taskType;

    /** 状态：待处理/处理中/完成/失败/已取消 */
    @Schema(title = "状态：待处理/处理中/完成/失败/已取消")
    @Excel(name = "状态：待处理/处理中/完成/失败/已取消")
    private String bizStatus;

    /** 优先级1-10 */
    @Schema(title = "优先级1-10")
    @Excel(name = "优先级1-10")
    private Integer priority;

    /** 关联业务类型 */
    @Schema(title = "关联业务类型")
    @Excel(name = "关联业务类型")
    private String relatedType;

    /** 关联业务ID */
    @Schema(title = "关联业务ID")
    @Excel(name = "关联业务ID")
    private Long relatedId;

    /** 任务配置 */
    @Schema(title = "任务配置")
    @Excel(name = "任务配置")
    private String config;

    /** 任务结果 */
    @Schema(title = "任务结果")
    @Excel(name = "任务结果")
    private String result;

    /** 进度0-100 */
    @Schema(title = "进度0-100")
    @Excel(name = "进度0-100")
    private Integer progressPercentage;

    /** 失败原因 */
    @Schema(title = "失败原因")
    @Excel(name = "失败原因")
    private String errorMessage;

    /** 费用 */
    @Schema(title = "费用")
    @Excel(name = "费用")
    private BigDecimal costAmount;

    /** 消耗token数 */
    @Schema(title = "消耗token数")
    @Excel(name = "消耗token数")
    private Integer tokensUsed;

    /** 使用模型 */
    @Schema(title = "使用模型")
    @Excel(name = "使用模型")
    private String modelUsed;

    /** 开始时间 */
    @Schema(title = "开始时间")
    @Excel(name = "开始时间")
    private Instant startTime;

    /** 完成时间 */
    @Schema(title = "完成时间")
    @Excel(name = "完成时间")
    private Instant completeTime;

    /** 删除人ID */
    @Schema(title = "删除人ID")
    private Long deleteId;

    /** 删除时间 */
    @Schema(title = "删除时间")
    private Instant deleteTime;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
