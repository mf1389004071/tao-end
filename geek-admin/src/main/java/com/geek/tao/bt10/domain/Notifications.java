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
 * 用户站内通知对象 notifications
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("notifications")
@Schema(title = "用户站内通知对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class Notifications extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 接收用户ID */
    @Schema(title = "接收用户ID")
    @Excel(name = "接收用户ID")
    private Long userId;

    /** 标题 */
    @Schema(title = "标题")
    @Excel(name = "标题")
    private String title;

    /** 正文 */
    @Schema(title = "正文")
    @Excel(name = "正文")
    private String content;

    /** 类型：系统/互动/订阅等 */
    @Schema(title = "类型：系统/互动/订阅等")
    @Excel(name = "类型：系统/互动/订阅等")
    private String notificationType;

    /** 关联业务类型 */
    @Schema(title = "关联业务类型")
    @Excel(name = "关联业务类型")
    private String relatedType;

    /** 关联业务ID */
    @Schema(title = "关联业务ID")
    @Excel(name = "关联业务ID")
    private Long relatedId;

    /** 是否已读 */
    @Schema(title = "是否已读")
    @Excel(name = "是否已读")
    private Boolean isRead;

    /** 阅读时间 */
    @Schema(title = "阅读时间")
    @Excel(name = "阅读时间")
    private Instant readTime;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
