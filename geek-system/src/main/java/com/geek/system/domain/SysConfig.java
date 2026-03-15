package com.geek.system.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Excel.ColumnType;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数配置表 sys_config
 * 
 * @author geek
 */
@Table("sys_config")
@Schema(title = "参数配置表")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfig extends BaseEntity {

    /** 参数主键 */
    @Schema(title = "参数主键")
    @Excel(name = "参数主键", cellType = ColumnType.NUMERIC)
    @Id
    private Long configId;

    /** 参数名称 */
    @Schema(title = "参数名称")
    @Excel(name = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    private String configName;

    /** 参数键名 */
    @Schema(title = "参数键名")
    @Excel(name = "参数键名")
    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;

    /** 参数键值 */
    @Schema(title = "参数键值")
    @Excel(name = "参数键值")
    private String configValue;

    /** 系统内置（Y是 N否） */
    @Schema(title = "系统内置（Y是 N否）")
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    private String configType;

    /** 配置状态（0正常 1停用） */
    @Schema(title = "配置状态（0正常 1停用）")
    @Excel(name = "配置状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 配置分类 */
    @Schema(title = "配置分类")
    @Excel(name = "配置分类")
    private String category;

    /** 是否对前端开放 */
    @Schema(title = "是否对前端开放")
    @Excel(name = "是否对前端开放")
    private Boolean isPublic;

    /** 值类型：字符串/数字/布尔/JSON.config_type */
    @Schema(title = "值类型：字符串/数字/布尔/JSON.config_type")
    @Excel(name = "值类型：字符串/数字/布尔/JSON.config_type")
    private String valueType;
}
