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
 * 小鹅通素材中心分组 xiaoe_material_groups
 */
@Table("xiaoe_material_groups")
@Schema(title = "小鹅通素材分组")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeMaterialGroups extends BaseEntity {

    @Schema(title = "主键")
    @Id
    private Long id;

    @Schema(title = "小鹅通分组ID")
    @Excel(name = "分组ID")
    private Long categoryId;

    @Schema(title = "素材类型 1/2/3")
    private Integer materialType;

    @Schema(title = "IMAGE/AUDIO/VIDEO")
    private String materialKind;

    private String appId;
    private Long parentId;

    @Excel(name = "分组名称")
    private String name;

    private String categorySort;
    private Integer categoryCount;
    private Integer typeCount;

    private String syncStatus;
    private Instant lastSyncTime;

    @Schema(title = "原始JSON")
    private String jsonData;

    private String status;
    private Integer delFlag;
}
