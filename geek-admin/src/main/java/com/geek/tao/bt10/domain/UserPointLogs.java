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
 * 用户积分收支流水对象 user_point_logs
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("user_point_logs")
@Schema(title = "用户积分收支流水对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPointLogs extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 用户ID(sys_user.user_id) */
    @Schema(title = "用户ID(sys_user.user_id)")
    @Excel(name = "用户ID(sys_user.user_id)")
    private Long userId;

    /** 行为类型：签到/发内容/邀请等 */
    @Schema(title = "行为类型：签到/发内容/邀请等")
    @Excel(name = "行为类型：签到/发内容/邀请等")
    private String actionType;

    /** 本次变动积分(正获得负消耗) */
    @Schema(title = "本次变动积分(正获得负消耗)")
    @Excel(name = "本次变动积分(正获得负消耗)")
    private Long points;

    /** 变动前积分余额 */
    @Schema(title = "变动前积分余额")
    @Excel(name = "变动前积分余额")
    private Long balanceBefore;

    /** 变动后积分余额 */
    @Schema(title = "变动后积分余额")
    @Excel(name = "变动后积分余额")
    private Long balanceAfter;

    /** 关联业务类型如EVENT/CONTENT */
    @Schema(title = "关联业务类型如EVENT/CONTENT")
    @Excel(name = "关联业务类型如EVENT/CONTENT")
    private String relatedType;

    /** 关联业务主键 */
    @Schema(title = "关联业务主键")
    @Excel(name = "关联业务主键")
    private Long relatedId;

    /** 非空表示身份域流水，不累计 user_profiles.points */
    @Schema(title = "身份编码")
    private String identityCode;

    /** 该笔积分过期时间 */
    @Schema(title = "该笔积分过期时间")
    @Excel(name = "该笔积分过期时间")
    private Instant expiredTime;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
