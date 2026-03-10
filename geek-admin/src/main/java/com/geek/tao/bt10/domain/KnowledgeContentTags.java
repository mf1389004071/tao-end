package com.geek.tao.bt10.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识内容与标签多对多关联对象 knowledge_content_tags
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("knowledge_content_tags")
@Schema(title = "知识内容与标签多对多关联对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeContentTags extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 知识内容ID */
    @Schema(title = "知识内容ID")
    @Excel(name = "知识内容ID")
    private Long contentId;

    /** 标签ID */
    @Schema(title = "标签ID")
    @Excel(name = "标签ID")
    private Long tagId;

    /** 排序 */
    @Schema(title = "排序")
    @Excel(name = "排序")
    private Integer orderNum;
}
