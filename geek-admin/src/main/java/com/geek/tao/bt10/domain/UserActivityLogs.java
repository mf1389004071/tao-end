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
 * 用户行为轨迹日志对象 user_activity_logs
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_activity_logs")
@Schema(title = "用户行为轨迹日志对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserActivityLogs extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 发生时间 */
    @Schema(title = "发生时间")
    @Excel(name = "发生时间")
    private Instant occurredTime;

    /** IP 地址 */
    @Schema(title = "IP 地址")
    @Excel(name = "IP 地址")
    private String ipAddress;

    /** 终端设备信息 */
    @Schema(title = "终端设备信息")
    @Excel(name = "终端设备信息")
    private String device;

    /** 事件类型：LOGIN/LOGOUT/SIGNIN/LIKE/COMMENT/SHARE/REGISTER_EVENT等 */
    @Schema(title = "事件类型：LOGIN/LOGOUT/SIGNIN/LIKE/COMMENT/SHARE/REGISTER_EVENT等")
    @Excel(name = "事件类型：LOGIN/LOGOUT/SIGNIN/LIKE/COMMENT/SHARE/REGISTER_EVENT等")
    private String eventType;

    /** 事件来源业务类型：EVENT/CONTENT/USER等 */
    @Schema(title = "事件来源业务类型：EVENT/CONTENT/USER等")
    @Excel(name = "事件来源业务类型：EVENT/CONTENT/USER等")
    private String eventSourceType;

    /** 事件来源业务ID */
    @Schema(title = "事件来源业务ID")
    @Excel(name = "事件来源业务ID")
    private Long eventSourceId;

    /** 事件标签列表(JSON) */
    @Schema(title = "事件标签列表(JSON)")
    @Excel(name = "事件标签列表(JSON)")
    private String eventTags;

    /** 本次积分变化(可为空，无变更为NULL) */
    @Schema(title = "本次积分变化(可为空，无变更为NULL)")
    @Excel(name = "本次积分变化(可为空，无变更为NULL)")
    private Integer pointChange;

    /** 变更前积分余额 */
    @Schema(title = "变更前积分余额")
    @Excel(name = "变更前积分余额")
    private Integer pointBalanceBefore;

    /** 变更后积分余额 */
    @Schema(title = "变更后积分余额")
    @Excel(name = "变更后积分余额")
    private Integer pointBalanceAfter;

    /** 本次贡献点变化 */
    @Schema(title = "本次贡献点变化")
    @Excel(name = "本次贡献点变化")
    private BigDecimal contribChange;

    /** 变更前贡献点余额 */
    @Schema(title = "变更前贡献点余额")
    @Excel(name = "变更前贡献点余额")
    private BigDecimal contribBalanceBefore;

    /** 变更后贡献点余额 */
    @Schema(title = "变更后贡献点余额")
    @Excel(name = "变更后贡献点余额")
    private BigDecimal contribBalanceAfter;

    /** 额外上下文(如停留时长、入口渠道等) */
    @Schema(title = "额外上下文(如停留时长、入口渠道等)")
    @Excel(name = "额外上下文(如停留时长、入口渠道等)")
    private String extra;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
