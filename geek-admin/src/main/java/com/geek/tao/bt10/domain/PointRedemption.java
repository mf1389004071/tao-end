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
 * 用户积分兑换记录对象 point_redemption
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("point_redemption")
@Schema(title = "用户积分兑换记录对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PointRedemption extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 兑换商品ID */
    @Schema(title = "兑换商品ID")
    @Excel(name = "兑换商品ID")
    private Long productId;

    /** 消耗积分 */
    @Schema(title = "消耗积分")
    @Excel(name = "消耗积分")
    private Integer pointsUsed;

    /** 状态：待发放/已完成/已取消/已过期 */
    @Schema(title = "状态：待发放/已完成/已取消/已过期")
    @Excel(name = "状态：待发放/已完成/已取消/已过期")
    private String bizStatus;

    /** 兑换码 */
    @Schema(title = "兑换码")
    @Excel(name = "兑换码")
    private String redemptionCode;

    /** 使用/核销时间 */
    @Schema(title = "使用/核销时间")
    @Excel(name = "使用/核销时间")
    private Instant usedTime;

    /** 过期时间 */
    @Schema(title = "过期时间")
    @Excel(name = "过期时间")
    private Instant expiredTime;

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
