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
 * 用户活动报名记录对象 event_join
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_join")
@Schema(title = "用户活动报名记录对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventJoin extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 活动ID */
    @Schema(title = "活动ID")
    @Excel(name = "活动ID")
    private Long eventId;

    /** 报名用户ID */
    @Schema(title = "报名用户ID")
    @Excel(name = "报名用户ID")
    private Long userId;

    /** 状态：已确认/已取消/已签到/缺席 */
    @Schema(title = "状态：已确认/已取消/已签到/缺席")
    @Excel(name = "状态：已确认/已取消/已签到/缺席")
    private String bizStatus;

    /** 报名表单填写数据 */
    @Schema(title = "报名表单填写数据")
    @Excel(name = "报名表单填写数据")
    private String joinForm;

    /** 支付状态：待付/已付/已退/失败/已取消 */
    @Schema(title = "支付状态：待付/已付/已退/失败/已取消")
    @Excel(name = "支付状态：待付/已付/已退/失败/已取消")
    private String paymentStatus;

    /** 支付金额 */
    @Schema(title = "支付金额")
    @Excel(name = "支付金额")
    private BigDecimal paymentAmount;

    /** 支付方式：贡献点/积分/混合/现金等 */
    @Schema(title = "支付方式：贡献点/积分/混合/现金等")
    @Excel(name = "支付方式：贡献点/积分/混合/现金等")
    private String paymentMethod;

    /** 支付单号 */
    @Schema(title = "支付单号")
    @Excel(name = "支付单号")
    private String paymentNo;

    /** 签到时间 */
    @Schema(title = "签到时间")
    @Excel(name = "签到时间")
    private Instant checkedInTime;

    /** 签到方式：二维码/定位/手动 */
    @Schema(title = "签到方式：二维码/定位/手动")
    @Excel(name = "签到方式：二维码/定位/手动")
    private String checkInMethod;

    /** 签到位置信息 */
    @Schema(title = "签到位置信息")
    @Excel(name = "签到位置信息")
    private String checkInLocation;

    /** 转化状态：无/有意向/已转化 */
    @Schema(title = "转化状态：无/有意向/已转化")
    @Excel(name = "转化状态：无/有意向/已转化")
    private String conversionStatus;

    /** 转化产品ID */
    @Schema(title = "转化产品ID")
    @Excel(name = "转化产品ID")
    private Long conversionProductId;

    /** 转化金额 */
    @Schema(title = "转化金额")
    @Excel(name = "转化金额")
    private BigDecimal conversionAmount;

    /** 转化备注 */
    @Schema(title = "转化备注")
    @Excel(name = "转化备注")
    private String conversionNotes;

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
