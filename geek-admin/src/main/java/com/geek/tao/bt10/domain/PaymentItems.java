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
 * 统一支付订单明细表对象 payment_items
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("payment_items")
@Schema(title = "统一支付订单明细表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentItems extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 支付订单ID */
    @Schema(title = "支付订单ID")
    @Excel(name = "支付订单ID")
    private Long paymentId;

    /** 明细类型：PRODUCT/SERVICE/COUPON/IDENTITY等 */
    @Schema(title = "明细类型：PRODUCT/SERVICE/COUPON/IDENTITY等")
    @Excel(name = "明细类型：PRODUCT/SERVICE/COUPON/IDENTITY等")
    private String itemType;

    /** 明细名称(商品/服务/身份等) */
    @Schema(title = "明细名称(商品/服务/身份等)")
    @Excel(name = "明细名称(商品/服务/身份等)")
    private String itemName;

    /** 原价(单价) */
    @Schema(title = "原价(单价)")
    @Excel(name = "原价(单价)")
    private BigDecimal originalAmount;

    /** 实际计价金额(单价，可为0表示赠送) */
    @Schema(title = "实际计价金额(单价，可为0表示赠送)")
    @Excel(name = "实际计价金额(单价，可为0表示赠送)")
    private BigDecimal amount;

    /** 数量 */
    @Schema(title = "数量")
    @Excel(name = "数量")
    private Integer quantity;

    /** 是否赠品 */
    @Schema(title = "是否赠品")
    @Excel(name = "是否赠品")
    private Boolean isGift;

    /** 是否可转让 */
    @Schema(title = "是否可转让")
    @Excel(name = "是否可转让")
    private Boolean isTransferable;

    /** 是否可帮买(代他人购买) */
    @Schema(title = "是否可帮买(代他人购买)")
    @Excel(name = "是否可帮买(代他人购买)")
    private Boolean canBuyForOthers;

    /** 使用状态：PENDING/USED/EXPIRED/CANCELLED */
    @Schema(title = "使用状态：PENDING/USED/EXPIRED/CANCELLED")
    @Excel(name = "使用状态：PENDING/USED/EXPIRED/CANCELLED")
    private String usageStatus;

    /** 发放方式：AUTO/MANUAL/COUPON等 */
    @Schema(title = "发放方式：AUTO/MANUAL/COUPON等")
    @Excel(name = "发放方式：AUTO/MANUAL/COUPON等")
    private String grantMethod;

    /** 关联业务类型：EVENT/COURSE/IDENTITY/POINT_PRODUCT等 */
    @Schema(title = "关联业务类型：EVENT/COURSE/IDENTITY/POINT_PRODUCT等")
    @Excel(name = "关联业务类型：EVENT/COURSE/IDENTITY/POINT_PRODUCT等")
    private String relatedType;

    /** 关联业务ID */
    @Schema(title = "关联业务ID")
    @Excel(name = "关联业务ID")
    private Long relatedId;

    /** 关联 biz_product.id */
    @Schema(title = "产品ID")
    private Long productId;

    /** 扩展JSON，如可转让规则等 */
    @Schema(title = "扩展JSON，如可转让规则等")
    @Excel(name = "扩展JSON，如可转让规则等")
    private String jsonData;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
