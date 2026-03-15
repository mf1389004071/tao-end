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
 * 用户身份关系表对象 user_identities
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_identities")
@Schema(title = "用户身份关系表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserIdentities extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 身份编码 */
    @Schema(title = "身份编码")
    @Excel(name = "身份编码")
    private String identityCode;

    /** 是否当前主身份 */
    @Schema(title = "是否当前主身份")
    @Excel(name = "是否当前主身份")
    private Boolean isPrimary;

    /** 状态：ACTIVE/EXPIRED/REVOKED */
    @Schema(title = "状态：ACTIVE/EXPIRED/REVOKED")
    @Excel(name = "状态：ACTIVE/EXPIRED/REVOKED")
    private String bizStatus;

    /** 获得时间 */
    @Schema(title = "获得时间")
    @Excel(name = "获得时间")
    private Instant acquiredTime;

    /** 到期时间(可为空) */
    @Schema(title = "到期时间(可为空)")
    @Excel(name = "到期时间(可为空)")
    private Instant expiredTime;

    /** 来源类型：PAYMENT/EVENT/MANUAL等 */
    @Schema(title = "来源类型：PAYMENT/EVENT/MANUAL等")
    @Excel(name = "来源类型：PAYMENT/EVENT/MANUAL等")
    private String sourceType;

    /** 来源业务ID(如支付订单ID) */
    @Schema(title = "来源业务ID(如支付订单ID)")
    @Excel(name = "来源业务ID(如支付订单ID)")
    private Long sourceId;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
