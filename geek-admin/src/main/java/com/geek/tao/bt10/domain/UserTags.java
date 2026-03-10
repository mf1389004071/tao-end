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
 * 用户与多维标签关联表对象 user_tags
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_tags")
@Schema(title = "用户与多维标签关联表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserTags extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 标签ID */
    @Schema(title = "标签ID")
    @Excel(name = "标签ID")
    private Long tagId;

    /** 权重(0-1或0-100，越大代表相关性越高) */
    @Schema(title = "权重(0-1或0-100，越大代表相关性越高)")
    @Excel(name = "权重(0-1或0-100，越大代表相关性越高)")
    private BigDecimal weight;

    /** 来源：SYSTEM/SELF/COACH等 */
    @Schema(title = "来源：SYSTEM/SELF/COACH等")
    @Excel(name = "来源：SYSTEM/SELF/COACH等")
    private String source;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
