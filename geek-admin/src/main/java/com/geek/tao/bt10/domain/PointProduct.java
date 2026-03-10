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
 * 积分商城商品对象 point_product
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("point_product")
@Schema(title = "积分商城商品对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PointProduct extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 商品名称 */
    @Schema(title = "商品名称")
    @Excel(name = "商品名称")
    private String name;

    /** 类型：优惠券/咨询/实物/会员等 */
    @Schema(title = "类型：优惠券/咨询/实物/会员等")
    @Excel(name = "类型：优惠券/咨询/实物/会员等")
    private String productType;

    /** 兑换所需积分 */
    @Schema(title = "兑换所需积分")
    @Excel(name = "兑换所需积分")
    private Integer pointsRequired;

    /** 库存数量，-1表示不限 */
    @Schema(title = "库存数量，-1表示不限")
    @Excel(name = "库存数量，-1表示不限")
    private Integer stockQuantity;

    /** 已兑换数量 */
    @Schema(title = "已兑换数量")
    @Excel(name = "已兑换数量")
    private Integer soldQuantity;

    /** 商品图 */
    @Schema(title = "商品图")
    @Excel(name = "商品图")
    private String imageUrl;

    /** 详情正文 */
    @Schema(title = "详情正文")
    @Excel(name = "详情正文")
    private String detailContent;

    /** 有效天数 */
    @Schema(title = "有效天数")
    @Excel(name = "有效天数")
    private Integer validDays;

    /** 状态：上架/下架/售罄 */
    @Schema(title = "状态：上架/下架/售罄")
    @Excel(name = "状态：上架/下架/售罄")
    private String bizStatus;

    /** 排序 */
    @Schema(title = "排序")
    @Excel(name = "排序")
    private Integer orderNum;

    /** 删除人ID */
    @Schema(title = "删除人ID")
    private Long deleteId;

    /** 删除时间 */
    @Schema(title = "删除时间")
    private Instant deleteTime;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

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
