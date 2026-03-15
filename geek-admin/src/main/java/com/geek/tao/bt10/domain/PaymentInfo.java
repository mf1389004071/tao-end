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
 * 统一支付订单对象 payment_info
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("payment_info")
@Schema(title = "统一支付订单对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentInfo extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 业务订单号 */
    @Schema(title = "业务订单号")
    @Excel(name = "业务订单号")
    private String orderNo;

    /** 下单用户ID */
    @Schema(title = "下单用户ID")
    @Excel(name = "下单用户ID")
    private Long userId;

    /** 订单类型：活动/商品/充值等 */
    @Schema(title = "订单类型：活动/商品/充值等")
    @Excel(name = "订单类型：活动/商品/充值等")
    private String orderType;

    /** 关联业务类型 */
    @Schema(title = "关联业务类型")
    @Excel(name = "关联业务类型")
    private String relatedType;

    /** 关联业务ID */
    @Schema(title = "关联业务ID")
    @Excel(name = "关联业务ID")
    private Long relatedId;

    /** 订单总金额 */
    @Schema(title = "订单总金额")
    @Excel(name = "订单总金额")
    private BigDecimal totalAmount;

    /** 贡献点支付额 */
    @Schema(title = "贡献点支付额")
    @Excel(name = "贡献点支付额")
    private BigDecimal contribAmount;

    /** 积分支付额 */
    @Schema(title = "积分支付额")
    @Excel(name = "积分支付额")
    private Integer pointsAmount;

    /** 现金支付额 */
    @Schema(title = "现金支付额")
    @Excel(name = "现金支付额")
    private BigDecimal cashAmount;

    /** 支付状态：待付/已付/已退/失败/已取消 */
    @Schema(title = "支付状态：待付/已付/已退/失败/已取消")
    @Excel(name = "支付状态：待付/已付/已退/失败/已取消")
    private String paymentStatus;

    /** 支付方式：微信/支付宝/贡献点/积分等 */
    @Schema(title = "支付方式：微信/支付宝/贡献点/积分等")
    @Excel(name = "支付方式：微信/支付宝/贡献点/积分等")
    private String paymentMethod;

    /** 第三方支付单号 */
    @Schema(title = "第三方支付单号")
    @Excel(name = "第三方支付单号")
    private String paymentNo;

    /** 支付成功时间 */
    @Schema(title = "支付成功时间")
    @Excel(name = "支付成功时间")
    private Instant paidTime;

    /** 退款金额 */
    @Schema(title = "退款金额")
    @Excel(name = "退款金额")
    private BigDecimal refundAmount;

    /** 退款原因 */
    @Schema(title = "退款原因")
    @Excel(name = "退款原因")
    private String refundReason;

    /** 退款时间 */
    @Schema(title = "退款时间")
    @Excel(name = "退款时间")
    private Instant refundedTime;

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

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
