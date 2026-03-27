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
 * 站内私信消息 private_message
 */
@Table("private_message")
@Schema(title = "私信消息")
@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateMessage extends BaseEntity {

    @Id
    private Long id;

    private Long threadId;

    private Long senderId;

    private Long receiverId;

    private String messageType;

    private String content;

    private Instant sentTime;

    private Instant readTime;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    private Integer delFlag;
}
