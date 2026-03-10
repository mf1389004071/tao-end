package com.geek.tao.bt10.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户成长阶段变更历史对象 user_growth
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_growth")
@Schema(title = "用户成长阶段变更历史对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserGrowth extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID(sys_user.user_id) */
    @Schema(title = "用户ID(sys_user.user_id)")
    @Excel(name = "用户ID(sys_user.user_id)")
    private Long userId;

    /** 原阶段 */
    @Schema(title = "原阶段")
    @Excel(name = "原阶段")
    private String stageFrom;

    /** 新阶段 */
    @Schema(title = "新阶段")
    @Excel(name = "新阶段")
    private String stageTo;

    /** 触发方式：自动/手动/任务完成等 */
    @Schema(title = "触发方式：自动/手动/任务完成等")
    @Excel(name = "触发方式：自动/手动/任务完成等")
    private String triggerType;

    /** 触发上下文数据 */
    @Schema(title = "触发上下文数据")
    @Excel(name = "触发上下文数据")
    private String triggerData;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
