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
 * 业务产品(现金/技能包) biz_product
 */
@Table("biz_product")
@Schema(title = "业务产品")
@Data
@EqualsAndHashCode(callSuper = true)
public class BizProduct extends BaseEntity {

    @Schema(title = "主键")
    @Id
    private Long id;

    @Schema(title = "产品编码")
    @Excel(name = "产品编码")
    private String code;

    @Schema(title = "产品名称")
    @Excel(name = "产品名称")
    private String name;

    @Schema(title = "产品类型")
    private String productType;

    @Schema(title = "现金标价")
    private BigDecimal priceAmount;

    @Schema(title = "续期天数")
    private Integer validDays;

    @Schema(title = "关联身份编码")
    private String identityCode;

    @Schema(title = "外部购买链接")
    private String buyUrl;

    @Schema(title = "封面图")
    private String coverImageUrl;

    @Schema(title = "详情")
    private String detailContent;

    @Schema(title = "上架状态")
    private String bizStatus;

    @Schema(title = "排序")
    private Integer orderNum;

    @Schema(title = "扩展JSON")
    private String jsonData;

    private Long deleteId;
    private java.time.Instant deleteTime;

    @Schema(title = "状态")
    private String status;

    private Integer delFlag;
}
