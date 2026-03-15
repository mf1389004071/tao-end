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
 * 合伙人创建的社群对象 community_info
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("community_info")
@Schema(title = "合伙人创建的社群对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityInfo extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 社群名称 */
    @Schema(title = "社群名称")
    @Excel(name = "社群名称")
    private String name;

    /** 创建者(合伙人)用户ID */
    @Schema(title = "创建者(合伙人)用户ID")
    @Excel(name = "创建者(合伙人)用户ID")
    private Long ownerId;

    /** 是否公开可见 */
    @Schema(title = "是否公开可见")
    @Excel(name = "是否公开可见")
    private Boolean isPublic;

    /** 最大成员数 */
    @Schema(title = "最大成员数")
    @Excel(name = "最大成员数")
    private Integer maxMembers;

    /** 当前成员数 */
    @Schema(title = "当前成员数")
    @Excel(name = "当前成员数")
    private Integer memberCount;

    /** 封面图 */
    @Schema(title = "封面图")
    @Excel(name = "封面图")
    private String coverImageUrl;

    /** 所在城市 */
    @Schema(title = "所在城市")
    @Excel(name = "所在城市")
    private String city;

    /** 状态：正常/已归档/已解散 */
    @Schema(title = "状态：正常/已归档/已解散")
    @Excel(name = "状态：正常/已归档/已解散")
    private String bizStatus;

    /** 扩展文本1 */
    @Schema(title = "扩展文本1")
    @Excel(name = "扩展文本1")
    private String text1;

    /** 扩展文本2 */
    @Schema(title = "扩展文本2")
    @Excel(name = "扩展文本2")
    private String text2;

    /** 扩展文本3 */
    @Schema(title = "扩展文本3")
    @Excel(name = "扩展文本3")
    private String text3;

    /** 扩展JSON */
    @Schema(title = "扩展JSON")
    @Excel(name = "扩展JSON")
    private String jsonData;

    /** 删除人ID */
    @Schema(title = "删除人ID")
    private Long deleteId;

    /** 删除时间 */
    @Schema(title = "删除时间")
    private Instant deleteTime;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;
}
