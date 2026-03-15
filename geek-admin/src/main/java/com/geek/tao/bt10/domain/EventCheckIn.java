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
 * 单次签到记录对象 event_check_in
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_check_in")
@Schema(title = "单次签到记录对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventCheckIn extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 报名记录ID */
    @Schema(title = "报名记录ID")
    @Excel(name = "报名记录ID")
    private Long joinId;

    /** 场次ID(周期活动时用) */
    @Schema(title = "场次ID(周期活动时用)")
    @Excel(name = "场次ID(周期活动时用)")
    private Long sessionId;

    /** 签到时间 */
    @Schema(title = "签到时间")
    @Excel(name = "签到时间")
    private Instant checkInTime;

    /** 签到方式：二维码/定位/手动 */
    @Schema(title = "签到方式：二维码/定位/手动")
    @Excel(name = "签到方式：二维码/定位/手动")
    private String checkInMethod;

    /** 签到位置 */
    @Schema(title = "签到位置")
    @Excel(name = "签到位置")
    private String checkInLocation;

    /** 操作人ID(手动签到时) */
    @Schema(title = "操作人ID(手动签到时)")
    @Excel(name = "操作人ID(手动签到时)")
    private Long operatorId;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
