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
 * 社群与用户的成员关系对象 community_member
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("community_member")
@Schema(title = "社群与用户的成员关系对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityMember extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 社群ID */
    @Schema(title = "社群ID")
    @Excel(name = "社群ID")
    private Long communityId;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 角色：群主/管理员/成员 */
    @Schema(title = "角色：群主/管理员/成员")
    @Excel(name = "角色：群主/管理员/成员")
    private String role;

    /** 状态：在群/已退出/被移出 */
    @Schema(title = "状态：在群/已退出/被移出")
    @Excel(name = "状态：在群/已退出/被移出")
    private String bizStatus;

    /** 加入时间 */
    @Schema(title = "加入时间")
    @Excel(name = "加入时间")
    private Instant joinedTime;

    /** 离开时间 */
    @Schema(title = "离开时间")
    @Excel(name = "离开时间")
    private Instant leftTime;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
