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
 * 周期活动的单场次对象 event_session
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_session")
@Schema(title = "周期活动的单场次对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventSession extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 所属活动ID */
    @Schema(title = "所属活动ID")
    @Excel(name = "所属活动ID")
    private Long eventId;

    /** 场次日期 */
    @Schema(title = "场次日期")
    @Excel(name = "场次日期")
    private Instant sessionDate;

    /** 场次开始时间 */
    @Schema(title = "场次开始时间")
    @Excel(name = "场次开始时间")
    private Instant startTime;

    /** 场次结束时间 */
    @Schema(title = "场次结束时间")
    @Excel(name = "场次结束时间")
    private Instant endTime;

    /** 状态：已排期/进行中/已结束/已取消 */
    @Schema(title = "状态：已排期/进行中/已结束/已取消")
    @Excel(name = "状态：已排期/进行中/已结束/已取消")
    private String bizStatus;

    /** 本场签到人数 */
    @Schema(title = "本场签到人数")
    @Excel(name = "本场签到人数")
    private Integer checkInCount;

    /** AI生成场次总结 */
    @Schema(title = "AI生成场次总结")
    @Excel(name = "AI生成场次总结")
    private String summaryText;

    /** 本场会议链接 */
    @Schema(title = "本场会议链接")
    @Excel(name = "本场会议链接")
    private String meetingUrl;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
