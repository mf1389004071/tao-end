package com.geek.tao.bt10.domain;

import java.math.BigDecimal;
import java.time.Instant;
import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 本平台用户与小鹅通用户ID映射对象 xiaoe_user_mapping
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("xiaoe_user_mapping")
@Schema(title = "本平台用户与小鹅通用户ID映射对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeUserMapping extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 本平台用户ID */
    @Schema(title = "本平台用户ID")
    @Excel(name = "本平台用户ID")
    private Long userId;

    /** 小鹅通用户ID */
    @Schema(title = "小鹅通用户ID")
    @Excel(name = "小鹅通用户ID")
    private String xiaoeUserId;

    /** 映射方式：自动/手动 */
    @Schema(title = "映射方式：自动/手动")
    @Excel(name = "映射方式：自动/手动")
    private String mappingType;

    /** 匹配置信度0-1 */
    @Schema(title = "匹配置信度0-1")
    @Excel(name = "匹配置信度0-1")
    private BigDecimal confidenceScore;

    /** 建立映射时间 */
    @Schema(title = "建立映射时间")
    @Excel(name = "建立映射时间")
    private Instant mappedTime;

    /** 采集手机号，空表示缺号 */
    @Schema(title = "采集手机号")
    @Excel(name = "手机号")
    private String phone;

    /** 缺号下次可被 umissing 下发的时间 */
    @Schema(title = "缺号下次可查时间")
    private Instant phoneNextQueryAt;

    /** 小鹅昵称快照 */
    @Schema(title = "昵称")
    @Excel(name = "昵称")
    private String nickName;

    /** 该用户最新上报原始快照 */
    @Schema(title = "原始快照JSON")
    private String jsonData;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
