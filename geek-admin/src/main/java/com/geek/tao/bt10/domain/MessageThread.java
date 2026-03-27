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
 * 站内私信会话 message_thread
 */
@Table("message_thread")
@Schema(title = "私信会话")
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageThread extends BaseEntity {

    @Id
    private Long id;

    @Schema(title = "参与者A（较小 user_id）")
    @Excel(name = "userAId")
    private Long userAId;

    @Schema(title = "参与者B（较大 user_id）")
    @Excel(name = "userBId")
    private Long userBId;

    private Long lastMessageId;

    private Instant lastMessageTime;

    private String lastMessagePreview;

    private Integer aUnreadCount;

    private Integer bUnreadCount;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志")
    private Integer delFlag;
}
