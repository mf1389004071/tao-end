package com.geek.tao.bt10.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户主页社交动作 user_social_action
 */
@Table("user_social_action")
@Schema(title = "用户主页社交动作")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSocialAction extends BaseEntity {

    @Id
    private Long id;

    private Long targetUserId;

    private Long actorUserId;

    private String actionType;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    private Integer delFlag;
}
