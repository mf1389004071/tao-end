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
 * 小鹅通订单同步表对象 xiaoe_orders
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("xiaoe_orders")
@Schema(title = "小鹅通订单同步表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeOrders extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 小鹅通订单号 */
    @Schema(title = "小鹅通订单号")
    @Excel(name = "小鹅通订单号")
    private String xiaoeOrderNo;

    /** 关联本平台用户ID */
    @Schema(title = "关联本平台用户ID")
    @Excel(name = "关联本平台用户ID")
    private Long userId;

    /** 小鹅通用户ID */
    @Schema(title = "小鹅通用户ID")
    @Excel(name = "小鹅通用户ID")
    private String xiaoeUserId;

    /** 小鹅通用户昵称 */
    @Schema(title = "小鹅通用户昵称")
    @Excel(name = "小鹅通用户昵称")
    private String xiaoeNickName;

    /** 小鹅通订单状态 */
    @Schema(title = "小鹅通订单状态")
    @Excel(name = "小鹅通订单状态")
    private String orderState;

    /** 订单状态描述 */
    @Schema(title = "订单状态描述")
    private String orderStateDesc;

    /** 订单类型码 */
    @Schema(title = "订单类型码")
    private String orderType;

    /** 订单类型描述 */
    @Schema(title = "订单类型描述")
    private String orderTypeDesc;

    /** 实际支付金额 */
    @Schema(title = "实际支付金额")
    @Excel(name = "实际支付金额")
    private BigDecimal actualFee;

    /** 商品单价(元) */
    @Schema(title = "商品单价")
    private BigDecimal unitPrice;

    /** 购买数量 */
    @Schema(title = "购买数量")
    private Integer quantity;

    /** 商品名称 */
    @Schema(title = "商品名称")
    @Excel(name = "商品名称")
    private String goodsName;

    /** 商品类型 */
    @Schema(title = "商品类型")
    @Excel(name = "商品类型")
    private String goodsType;

    /** SPU类型 */
    @Schema(title = "SPU类型")
    @Excel(name = "SPU类型")
    private String spuType;

    /** 支付状态 */
    @Schema(title = "支付状态")
    @Excel(name = "支付状态")
    private String payState;

    /** 支付方式（描述） */
    @Schema(title = "支付方式")
    @Excel(name = "支付方式")
    private String payType;

    /** 支付方式码 */
    @Schema(title = "支付方式码")
    private String payTypeCode;

    /** 配送方式码 */
    @Schema(title = "配送方式码")
    private String shipWay;

    /** 配送方式描述 */
    @Schema(title = "配送方式")
    private String shipWayDesc;

    /** 买家手机号 */
    @Schema(title = "买家手机号")
    private String buyerPhone;

    /** 渠道来源 */
    @Schema(title = "渠道来源")
    private String channelSource;

    /** 小鹅通 app_id */
    @Schema(title = "小鹅通appId")
    private String appId;

    /** 第三方交易号 */
    @Schema(title = "第三方交易号")
    @Excel(name = "第三方交易号")
    private String tradeNo;

    /** 小鹅通侧创建时间 */
    @Schema(title = "小鹅通侧创建时间")
    @Excel(name = "小鹅通侧创建时间")
    private Instant xiaoeCreateTime;

    /** 学员信息快照 */
    @Schema(title = "学员信息快照")
    @Excel(name = "学员信息快照")
    private String studentInfo;

    /** 发票信息 */
    @Schema(title = "发票信息")
    @Excel(name = "发票信息")
    private String invoiceInfo;

    /** 同步状态：待同步/已同步/失败 */
    @Schema(title = "同步状态：待同步/已同步/失败")
    @Excel(name = "同步状态：待同步/已同步/失败")
    private String syncStatus;

    /** 最近同步时间 */
    @Schema(title = "最近同步时间")
    @Excel(name = "最近同步时间")
    private Instant lastSyncTime;

    /** 本地订单号 */
    @Schema(title = "本地订单号")
    @Excel(name = "本地订单号")
    private String orderNo;

    /** 处理状态 */
    @Schema(title = "处理状态")
    @Excel(name = "处理状态")
    private String processStatus;

    /** 待兑/购买人手机号 */
    @Schema(title = "待兑手机号")
    private String claimPhone;

    /** 关联 biz_product.id */
    @Schema(title = "产品ID")
    private Long productId;

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

    /** 原始数据 */
    @Schema(title = "原始数据")
    @Excel(name = "原始数据")
    private String jsonData;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
