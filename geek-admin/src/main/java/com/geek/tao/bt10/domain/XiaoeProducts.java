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
 * 小鹅通产品资源同步表 xiaoe_products（系列课/专栏/大专栏）
 */
@Table("xiaoe_products")
@Schema(title = "小鹅通产品资源")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeProducts extends BaseEntity {

    @Schema(title = "主键")
    @Id
    private Long id;

    @Schema(title = "小鹅通资源ID")
    @Excel(name = "资源ID")
    private String resourceId;

    @Schema(title = "资源类型码")
    private Integer resourceType;

    @Schema(title = "产品种类 CAMP_PRO/COLUMN/BIG_COLUMN")
    private String productKind;

    @Schema(title = "小鹅通 app_id")
    private String appId;

    @Schema(title = "标题")
    @Excel(name = "标题")
    private String title;

    @Schema(title = "简介")
    private String summary;

    @Schema(title = "封面图")
    private String imgUrl;

    @Schema(title = "压缩封面图")
    private String imgUrlCompressed;

    @Schema(title = "H5链接")
    private String h5Url;

    @Schema(title = "售价(元)")
    private BigDecimal price;

    @Schema(title = "划线价(元)")
    private BigDecimal linePrice;

    @Schema(title = "售卖形态")
    private Integer goodsType;

    @Schema(title = "售卖类型")
    private Integer sellType;

    @Schema(title = "上架状态")
    private Integer saleStatus;

    @Schema(title = "审核状态")
    private Integer auditStatus;

    @Schema(title = "授权类型")
    private Integer authType;

    @Schema(title = "保护状态")
    private Integer protectStatus;

    @Schema(title = "版本ID")
    private String versionId;

    @Schema(title = "排序位置")
    private Integer position;

    private Integer isFree;
    private Integer isPublic;
    private Integer isPassword;
    private Integer isStopSell;
    private Integer isDisplay;
    private Integer isBan;
    private Integer isForceUnshelve;
    private Integer isJoinMarketAct;
    private Integer isTranscode;

    private Instant saleAt;
    private Instant canSoldStart;
    private Instant canSoldEnd;

    private Integer periodType;
    private String periodValue;
    private String periodJson;

    private Integer userCount;
    private Integer viewCount;
    private Integer resourceCnt;
    private Integer interactiveCnt;
    private Integer subCourseCnt;

    private Instant lastUpdatedAt;
    private Instant curriculumTime;
    private Instant curriculumEndTime;
    private Integer createdSource;

    private String belongUserInfo;
    private String createdByResourceInfo;

    private String syncStatus;
    private Instant lastSyncTime;

    @Schema(title = "列表项原始JSON")
    private String jsonData;

    private String status;
    private Integer delFlag;
}
