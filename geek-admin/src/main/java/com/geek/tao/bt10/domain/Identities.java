package com.geek.tao.bt10.domain;

import java.math.BigDecimal;
import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统身份定义表对象 identities
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("identities")
@Schema(title = "系统身份定义表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class Identities extends BaseEntity
{


    /** 身份编码(如 FOUNDER/PARTNER) */
    @Schema(title = "身份编码(如 FOUNDER/PARTNER)")
    @Id
    private String identityCode;

    /** 身份数值等级(如 0/10/20/30/50/60) */
    @Schema(title = "身份数值等级(如 0/10/20/30/50/60)")
    @Excel(name = "身份数值等级(如 0/10/20/30/50/60)")
    private Integer identityLevel;

    /** 身份名称，如 创始人/联创/合伙人/高手/粉丝/城市主理人/合作方 */
    @Schema(title = "身份名称，如 创始人/联创/合伙人/高手/粉丝/城市主理人/合作方")
    @Excel(name = "身份名称，如 创始人/联创/合伙人/高手/粉丝/城市主理人/合作方")
    private String name;

    /** 身份图标URL或名称 */
    @Schema(title = "身份图标URL或名称")
    @Excel(name = "身份图标URL或名称")
    private String icon;

    /** 身份主题色 */
    @Schema(title = "身份主题色")
    @Excel(name = "身份主题色")
    private String themeColor;

    /** 身份简介 */
    @Schema(title = "身份简介")
    @Excel(name = "身份简介")
    private String intro;

    /** 系统法律说明 */
    @Schema(title = "系统法律说明")
    @Excel(name = "系统法律说明")
    private String legalText;

    /** 权利说明 */
    @Schema(title = "权利说明")
    @Excel(name = "权利说明")
    private String rightsText;

    /** 责任说明 */
    @Schema(title = "责任说明")
    @Excel(name = "责任说明")
    private String dutiesText;

    /** 利益说明 */
    @Schema(title = "利益说明")
    @Excel(name = "利益说明")
    private String benefitsText;

    /** 晋升说明 */
    @Schema(title = "晋升说明")
    @Excel(name = "晋升说明")
    private String upgradeRulesText;

    /** 身份付费金额(人民币) */
    @Schema(title = "身份付费金额(人民币)")
    @Excel(name = "身份付费金额(人民币)")
    private BigDecimal priceAmount;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
