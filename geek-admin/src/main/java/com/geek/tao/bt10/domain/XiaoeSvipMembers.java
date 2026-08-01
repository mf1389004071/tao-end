package com.geek.tao.bt10.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小鹅通超级会员成员同步表 xiaoe_svip_members（多等级同表）
 */
@Table("xiaoe_svip_members")
@Schema(title = "小鹅通超级会员成员")
@Data
@EqualsAndHashCode(callSuper = true)
public class XiaoeSvipMembers extends BaseEntity {

    @Schema(title = "主键")
    @Id
    private Long id;

    @Schema(title = "小鹅通会员记录ID")
    private Long memberRecId;

    private String appId;
    private String svipId;

    @Excel(name = "等级名称")
    private String svipTitle;

    private String svipSubTitle;
    private Integer svipLevel;
    private Integer svipState;
    private String svipPageUrl;

    private String specId;
    private BigDecimal specPrice;
    private Integer specPeriod;
    private Integer specUnit;

    @Excel(name = "用户ID")
    private String userId;

    private String unionId;
    private Integer identityType;
    private LocalDate startTime;
    private LocalDate endTime;
    private Integer isForever;
    private Integer memberState;
    private Integer expirationDays;

    @Excel(name = "昵称")
    private String nickName;

    private String realName;
    private String wxAvatar;
    private String phoneNumber;
    private String collectionPhone;
    private Integer isSeal;
    private String birth;
    private Integer age;
    private Integer wxGender;
    private String industry;
    private String company;
    private String job;
    private String area;
    private String address;
    private String userFrom;
    private String belongPromoter;
    private Integer isWeworkCustomer;
    private Integer buyTimes;
    private BigDecimal payMoney;
    private Instant firstPayTime;
    private Instant lastestPayTime;
    private Instant latestVisitedAt;
    private Instant userCreatedAt;

    private String userTags;
    private String corpTags;
    private String followUsers;

    private String syncStatus;
    private Instant lastSyncTime;
    private String jsonData;

    private String status;
    private Integer delFlag;
}
