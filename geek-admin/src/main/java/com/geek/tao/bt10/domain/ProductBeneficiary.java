package com.geek.tao.bt10.domain;

import java.math.BigDecimal;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品多方分润配置 product_beneficiary
 */
@Table("product_beneficiary")
@Schema(title = "产品分润受益人")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductBeneficiary extends BaseEntity {

    @Id
    private Long id;
    private Long productId;
    private Long userId;
    private String roleCode;
    private BigDecimal shareRatio;
    private String currency;
    private String status;
    private Integer delFlag;
}
