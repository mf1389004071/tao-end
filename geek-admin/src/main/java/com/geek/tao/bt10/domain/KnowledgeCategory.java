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
 * 知识库分类对象 knowledge_category
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("knowledge_category")
@Schema(title = "知识库分类对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeCategory extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 分类名称 */
    @Schema(title = "分类名称")
    @Excel(name = "分类名称")
    private String name;

    /** URL/唯一标识 */
    @Schema(title = "URL/唯一标识")
    @Excel(name = "URL/唯一标识")
    private String slug;

    /** 父分类ID */
    @Schema(title = "父分类ID")
    @Excel(name = "父分类ID")
    private Long parentId;

    /** 排序 */
    @Schema(title = "排序")
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 图标 */
    @Schema(title = "图标")
    @Excel(name = "图标")
    private String icon;

    /** 主题色 */
    @Schema(title = "主题色")
    @Excel(name = "主题色")
    private String color;

    /** 可见权限等级1-5 */
    @Schema(title = "可见权限等级1-5")
    @Excel(name = "可见权限等级1-5")
    private Integer permissionLevel;

    /** 删除人ID */
    @Schema(title = "删除人ID")
    private Long deleteId;

    /** 删除时间 */
    @Schema(title = "删除时间")
    private Instant deleteTime;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

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
}
