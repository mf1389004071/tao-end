package com.geek.system.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Xss;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告表 sys_notice
 * 
 * @author geek
 */
@Schema(title = "系统访问记录表")
@Table("sys_notice")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNotice extends BaseEntity {

    /** 公告ID */
    @Schema(title = "公告ID")
    @Id
    private Long noticeId;

    /** 公告标题 */
    @Schema(title = "公告标题")
    @Excel(name = "公告标题")
    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    @Schema(title = "公告类型")
    @Excel(name = "公告类型", readConverterExp = "1=通知,2=公告")
    private String noticeType;

    /** 公告内容 */
    @Schema(title = "公告内容")
    @Excel(name = "公告内容")
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    @Schema(title = "公告状态")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
