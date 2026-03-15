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
 * 知识内容评论与回复，支持楼中楼与置顶对象 knowledge_comment
 *
 * @author qm.wu
 * @date 2026-03-08
 */
@Table("knowledge_comment")
@Schema(title = "知识内容评论与回复，支持楼中楼与置顶对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeComment extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 知识内容ID */
    @Schema(title = "知识内容ID")
    @Excel(name = "知识内容ID")
    private Long contentId;

    /** 评论人ID */
    @Schema(title = "评论人ID")
    @Excel(name = "评论人ID")
    private Long userId;

    /** 父评论ID(回复) */
    @Schema(title = "父评论ID(回复)")
    @Excel(name = "父评论ID(回复)")
    private Long parentId;

    /** 评论正文 */
    @Schema(title = "评论正文")
    @Excel(name = "评论正文")
    private String content;

    /** 点赞数 */
    @Schema(title = "点赞数")
    @Excel(name = "点赞数")
    private Integer likeCount;

    /** 是否置顶 */
    @Schema(title = "是否置顶")
    @Excel(name = "是否置顶")
    private Boolean isPinned;

    /** 状态：已发布/隐藏 */
    @Schema(title = "状态：已发布/隐藏")
    @Excel(name = "状态：已发布/隐藏")
    private String bizStatus;

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
