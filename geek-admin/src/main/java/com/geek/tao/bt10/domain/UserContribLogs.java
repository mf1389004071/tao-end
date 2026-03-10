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
 * 用户贡献点收支流水对象 user_contrib_logs
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_contrib_logs")
@Schema(title = "用户贡献点收支流水对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserContribLogs extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID(sys_user.user_id) */
    @Schema(title = "用户ID(sys_user.user_id)")
    @Excel(name = "用户ID(sys_user.user_id)")
    private Long userId;

    /** 类型：充值/购买/退款/奖励等 */
    @Schema(title = "类型：充值/购买/退款/奖励等")
    @Excel(name = "类型：充值/购买/退款/奖励等")
    private String actionType;

    /** 本次变动金额 */
    @Schema(title = "本次变动金额")
    @Excel(name = "本次变动金额")
    private BigDecimal amount;

    /** 变动前贡献点余额 */
    @Schema(title = "变动前贡献点余额")
    @Excel(name = "变动前贡献点余额")
    private BigDecimal balanceBefore;

    /** 变动后贡献点余额 */
    @Schema(title = "变动后贡献点余额")
    @Excel(name = "变动后贡献点余额")
    private BigDecimal balanceAfter;

    /** 关联业务类型 */
    @Schema(title = "关联业务类型")
    @Excel(name = "关联业务类型")
    private String relatedType;

    /** 关联业务ID */
    @Schema(title = "关联业务ID")
    @Excel(name = "关联业务ID")
    private Long relatedId;

    /** 支付单号 */
    @Schema(title = "支付单号")
    @Excel(name = "支付单号")
    private String paymentNo;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
