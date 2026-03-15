package com.geek.tao.bt10.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用标签定义表对象 tags
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("tags")
@Schema(title = "通用标签定义表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class Tags extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 标签类型：ABILITY/INTEREST/INDUSTRY/RESOURCE/NEED等 */
    @Schema(title = "标签类型：ABILITY/INTEREST/INDUSTRY/RESOURCE/NEED等")
    @Excel(name = "标签类型：ABILITY/INTEREST/INDUSTRY/RESOURCE/NEED等")
    private String tagType;

    /** 标签编码(同类型内唯一) */
    @Schema(title = "标签编码(同类型内唯一)")
    @Excel(name = "标签编码(同类型内唯一)")
    private String code;

    /** 标签名称 */
    @Schema(title = "标签名称")
    @Excel(name = "标签名称")
    private String name;

    /** 父标签ID(可选) */
    @Schema(title = "父标签ID(可选)")
    @Excel(name = "父标签ID(可选)")
    private Long parentId;

    /** 排序 */
    @Schema(title = "排序")
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
