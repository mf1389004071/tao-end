package com.geek.tao.bt10.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动与标签多对多关联对象 event_info_tags
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_info_tags")
@Schema(title = "活动与标签多对多关联对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventInfoTags extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 活动ID */
    @Schema(title = "活动ID")
    @Excel(name = "活动ID")
    private Long eventId;

    /** 标签ID */
    @Schema(title = "标签ID")
    @Excel(name = "标签ID")
    private Long tagId;

    /** 排序 */
    @Schema(title = "排序")
    @Excel(name = "排序")
    private Integer orderNum;
}
