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
 * 知识库内容对象 knowledge_content
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("knowledge_content")
@Schema(title = "知识库内容对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeContent extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 标题 */
    @Schema(title = "标题")
    @Excel(name = "标题")
    private String title;

    /** 副标题 */
    @Schema(title = "副标题")
    @Excel(name = "副标题")
    private String subtitle;

    /** 正文内容 */
    @Schema(title = "正文内容")
    @Excel(name = "正文内容")
    private String content;

    /** 类型：文章/工具/案例/Wiki等 */
    @Schema(title = "类型：文章/工具/案例/Wiki等")
    @Excel(name = "类型：文章/工具/案例/Wiki等")
    private String contentType;

    /** 所属分类ID */
    @Schema(title = "所属分类ID")
    @Excel(name = "所属分类ID")
    private Long categoryId;

    /** 作者用户ID */
    @Schema(title = "作者用户ID")
    @Excel(name = "作者用户ID")
    private Long authorId;

    /** 创始人/发起人ID */
    @Schema(title = "创始人/发起人ID")
    @Excel(name = "创始人/发起人ID")
    private Long founderId;

    /** 重点贡献人(逗号分隔) */
    @Schema(title = "重点贡献人(逗号分隔)")
    @Excel(name = "重点贡献人(逗号分隔)")
    private String keyContributors;

    /** 内容负责人ID */
    @Schema(title = "内容负责人ID")
    @Excel(name = "内容负责人ID")
    private Long managerId;

    /** 宣传语 */
    @Schema(title = "宣传语")
    @Excel(name = "宣传语")
    private String promotionalText;

    /** 核心价值观描述 */
    @Schema(title = "核心价值观描述")
    @Excel(name = "核心价值观描述")
    private String coreValues;

    /** 标签(逗号分隔) */
    @Schema(title = "标签(逗号分隔)")
    @Excel(name = "标签(逗号分隔)")
    private String tags;

    /** 状态：草稿/审核中/已发布/归档/已删 */
    @Schema(title = "状态：草稿/审核中/已发布/归档/已删")
    @Excel(name = "状态：草稿/审核中/已发布/归档/已删")
    private String bizStatus;

    /** 发布时间 */
    @Schema(title = "发布时间")
    @Excel(name = "发布时间")
    private Instant publishTime;

    /** 浏览次数 */
    @Schema(title = "浏览次数")
    @Excel(name = "浏览次数")
    private Long viewCount;

    /** 点赞数 */
    @Schema(title = "点赞数")
    @Excel(name = "点赞数")
    private Long likeCount;

    /** 评论数 */
    @Schema(title = "评论数")
    @Excel(name = "评论数")
    private Long commentCount;

    /** 分享数 */
    @Schema(title = "分享数")
    @Excel(name = "分享数")
    private Long shareCount;

    /** 收藏数 */
    @Schema(title = "收藏数")
    @Excel(name = "收藏数")
    private Long collectCount;

    /** SEO标题 */
    @Schema(title = "SEO标题")
    @Excel(name = "SEO标题")
    private String seoTitle;

    /** SEO描述 */
    @Schema(title = "SEO描述")
    @Excel(name = "SEO描述")
    private String seoDescription;

    /** SEO关键词 */
    @Schema(title = "SEO关键词")
    @Excel(name = "SEO关键词")
    private String seoKeywords;

    /** 来源 */
    @Schema(title = "来源")
    @Excel(name = "来源")
    private String sourceFrom;

    /** 难度等级 */
    @Schema(title = "难度等级")
    @Excel(name = "难度等级")
    private String difficultyLevel;

    /** AI生成摘要 */
    @Schema(title = "AI生成摘要")
    @Excel(name = "AI生成摘要")
    private String aiSummary;

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
