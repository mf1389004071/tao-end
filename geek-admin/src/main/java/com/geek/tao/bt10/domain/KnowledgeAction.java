package com.geek.tao.bt10.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户对知识内容的行为记录对象 knowledge_action
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("knowledge_action")
@Schema(title = "用户对知识内容的行为记录对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeAction extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 知识内容ID */
    @Schema(title = "知识内容ID")
    @Excel(name = "知识内容ID")
    private Long contentId;

    /** 类型：点赞/反对/收藏/分享等 */
    @Schema(title = "类型：点赞/反对/收藏/分享等")
    @Excel(name = "类型：点赞/反对/收藏/分享等")
    private String actionType;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
