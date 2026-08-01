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
 * 小鹅通素材中心内容 xiaoe_materials
 */
@Table("xiaoe_materials")
@Schema(title = "小鹅通素材内容")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeMaterials extends BaseEntity {

    @Schema(title = "主键")
    @Id
    private Long id;

    @Schema(title = "小鹅通素材ID")
    @Excel(name = "素材ID")
    private String materialId;

    private Long materialRecId;
    private String appId;
    private Integer materialType;
    private String materialKind;
    private Integer subType;

    @Excel(name = "标题")
    private String title;

    private String url;
    private String showUrl;
    private String downloadUrl;

    @Schema(title = "原始下载链接")
    @Excel(name = "原始链接")
    private String originalUrl;

    private Long categoryId;
    private String categoryName;
    private String creatorId;
    private String creatorName;
    private String materialSize;
    private String fileId;
    private Integer width;
    private Integer height;
    private BigDecimal lengthSec;
    private String pixelData;
    private String patchImgUrl;
    private String materialProperty;

    private Integer state;
    private Integer auditState;
    private Integer bannedState;
    private Integer materialState;
    private Integer materialStatus;
    private Integer dealState;
    private Integer decodeState;
    private String decodeDescription;
    private Integer reauditStatus;
    private Integer protectionStatus;
    private Integer materialSource;
    private Integer costSummary;
    private Integer referCount;
    private Integer viewCount;
    private String bannedReason;
    private Instant bannedAt;
    private Instant latestViewAt;
    private Instant materialCreatedAt;
    private Instant materialUpdatedAt;
    private String extendData;

    private String syncStatus;
    private Instant lastSyncTime;

    @Schema(title = "列表项原始JSON")
    private String jsonData;

    private String status;
    private Integer delFlag;
}
