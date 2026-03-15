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
 * 系统级通知与公告对象 notices
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("notices")
@Schema(title = "系统级通知与公告对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class Notices extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 标题 */
    @Schema(title = "标题")
    @Excel(name = "标题")
    private String title;

    /** 正文 */
    @Schema(title = "正文")
    @Excel(name = "正文")
    private String content;

    /** 类型：system等 */
    @Schema(title = "类型：system等")
    @Excel(name = "类型：system等")
    private String type;

    /** 是否紧急 */
    @Schema(title = "是否紧急")
    @Excel(name = "是否紧急")
    private Boolean isUrgent;

    /** 发布时间 */
    @Schema(title = "发布时间")
    @Excel(name = "发布时间")
    private Instant publishTime;

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

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
