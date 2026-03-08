package com.geek.system.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 文件对象 sys_file_info
 * 
 * @author geek
 * @date 2025-04-25
 */
@Schema(description = "文件对象")
@Table("sys_file_info")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFileInfo extends BaseEntity {

    /** 文件主键 */
    @Schema(title = "文件主键")
    @Id
    private Long fileId;

    /** 原始文件名 */
    @Schema(title = "原始文件名")
    @Excel(name = "原始文件名")
    private String fileName;

    /** 统一逻辑路径（/开头） */
    @Schema(title = "统一逻辑路径（/开头）")
    @Excel(name = "统一逻辑路径", readConverterExp = "/=开头")
    private String filePath;

    /** 存储类型（local/minio/oss） */
    @Schema(title = "存储类型（local/minio/oss）")
    @Excel(name = "存储类型", readConverterExp = "l=ocal/minio/oss")
    private String storageType;

    /** 文件类型/后缀 */
    @Schema(title = "文件类型/后缀")
    @Excel(name = "文件类型/后缀")
    private String fileType;

    /** 文件大小（字节） */
    @Schema(title = "文件大小（字节）")
    @Excel(name = "文件大小", readConverterExp = "字=节")
    private Long fileSize;

    /** 文件MD5 */
    @Schema(title = "文件MD5")
    @Excel(name = "文件MD5")
    private String md5;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 所属文件夹ID */
    @Schema(title = "所属文件夹ID")
    @Excel(name = "所属文件夹ID")
    private String folderId;

    /** MIME类型 */
    @Schema(title = "MIME类型")
    @Excel(name = "MIME类型")
    private String mimeType;

    /** 图片宽度 */
    @Schema(title = "图片宽度")
    @Excel(name = "图片宽度")
    private String width;

    /** 图片高度 */
    @Schema(title = "图片高度")
    @Excel(name = "图片高度")
    private String height;

    /** 音视频时长(秒) */
    @Schema(title = "音视频时长(秒)")
    @Excel(name = "音视频时长(秒)")
    private String duration;

    /** 删除人ID */
    @Schema(title = "删除人ID")
    @Excel(name = "删除人ID")
    private String deleteId;

    /** 删除时间(软删除) */
    @Schema(title = "删除时间(软删除)")
    @Excel(name = "删除时间(软删除)")
    private LocalDate deleteTime;

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
}
