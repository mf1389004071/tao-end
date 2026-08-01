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
 * 小鹅通内容资源同步表 xiaoe_contents（图文/音频/视频）
 */
@Table("xiaoe_contents")
@Schema(title = "小鹅通内容资源")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeContents extends BaseEntity {

    @Schema(title = "主键")
    @Id
    private Long id;

    @Schema(title = "小鹅通资源ID")
    @Excel(name = "资源ID")
    private String resourceId;

    @Schema(title = "资源类型码 1/2/3")
    private Integer resourceType;

    @Schema(title = "内容种类 TEXT/AUDIO/VIDEO")
    private String contentKind;

    @Schema(title = "小鹅通 app_id")
    private String appId;

    @Schema(title = "标题")
    @Excel(name = "标题")
    private String title;

    private String imgUrl;
    private String imgUrlCompressed;
    private String h5Url;

    private BigDecimal price;
    private BigDecimal linePrice;

    private Integer goodsType;
    private Integer sellType;
    private Integer saleStatus;
    private Integer auditStatus;
    private Integer authType;
    private Integer protectStatus;
    private String versionId;
    private Integer position;
    private Integer viewCount;

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
    private Integer periodType;
    private String periodValue;
    private String periodJson;

    private String syncStatus;
    private Instant lastSyncTime;

    @Schema(title = "列表项原始JSON")
    private String jsonData;

    private String status;
    private Integer delFlag;
}
