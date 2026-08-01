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
 * 小鹅通商城商品 xiaoe_goods（知识/实物/组合）
 */
@Table("xiaoe_goods")
@Schema(title = "小鹅通商城商品")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeGoods extends BaseEntity {

    @Id
    private Long id;

    @Excel(name = "SPU")
    private String spuId;

    private String resourceId;

    @Excel(name = "商品种类")
    private String goodsKind;

    private String appId;
    private Integer resourceType;
    private String spuType;
    private String spuTypeName;

    @Excel(name = "商品名称")
    private String goodsName;

    private String goodsSn;
    private String goodsImg;
    private String imgUrlCompressed;

    private BigDecimal priceLow;
    private BigDecimal priceHigh;
    private BigDecimal priceLine;

    private Integer saleStatus;
    private Integer sellMode;
    private Integer sellType;
    private Integer isDisplay;
    private Integer isForbid;
    private Integer isFree;
    private Integer isPassword;
    private Integer isPublic;
    private Integer isStopSell;
    private Integer isTimingSale;

    private Instant saleAt;
    private String timingSale;
    private Instant timingOfftime;

    private String resourceUrl;
    private String shortUrl;
    private String goodsCategoryId;
    private Long newCategoryIdV2;
    private Integer distributionPattern;

    private Integer stock;
    private Integer sellNum;
    private Integer visitNum;
    private Integer pv;
    private Integer uv;
    private Integer attachCount;

    private String period;
    private Integer periodType;
    private String periodValue;

    private String skuJson;
    private String categoryJson;
    private String extendJson;
    private String stockJson;

    private Instant goodsCreatedAt;
    private String syncStatus;
    private Instant lastSyncTime;
    private String jsonData;

    private String status;
    private Integer delFlag;
}
