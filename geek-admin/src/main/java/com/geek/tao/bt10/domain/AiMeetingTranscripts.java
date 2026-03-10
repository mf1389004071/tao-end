package com.geek.tao.bt10.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 音视频转写与AI摘要对象 ai_meeting_transcripts
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("ai_meeting_transcripts")
@Schema(title = "音视频转写与AI摘要对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class AiMeetingTranscripts extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 关联活动ID */
    @Schema(title = "关联活动ID")
    @Excel(name = "关联活动ID")
    private Long eventId;

    /** 关联场次ID */
    @Schema(title = "关联场次ID")
    @Excel(name = "关联场次ID")
    private Long sessionId;

    /** 原始音视频文件ID */
    @Schema(title = "原始音视频文件ID")
    @Excel(name = "原始音视频文件ID")
    private Long fileId;

    /** 转写JSON(时间戳与说话人) */
    @Schema(title = "转写JSON(时间戳与说话人)")
    @Excel(name = "转写JSON(时间戳与说话人)")
    private String transcriptJson;

    /** 完整文本 */
    @Schema(title = "完整文本")
    @Excel(name = "完整文本")
    private String fullText;

    /** AI摘要 */
    @Schema(title = "AI摘要")
    @Excel(name = "AI摘要")
    private String summary;

    /** 关键点 */
    @Schema(title = "关键点")
    @Excel(name = "关键点")
    private String keyPoints;

    /** 行动清单 */
    @Schema(title = "行动清单")
    @Excel(name = "行动清单")
    private String actionItems;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
