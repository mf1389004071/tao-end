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
 * 活动或线下课程主表对象 event_info
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("event_info")
@Schema(title = "活动或线下课程主表对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventInfo extends BaseEntity
{


    /** 主键 */
    @Schema(title = "主键")
    @Id
    private Long id;

    /** 活动标题 */
    @Schema(title = "活动标题")
    @Excel(name = "活动标题")
    private String title;

    /** 参与方式：线上/线下/混合 */
    @Schema(title = "参与方式：线上/线下/混合")
    @Excel(name = "参与方式：线上/线下/混合")
    private String joinType;

    /** 活动分类 */
    @Schema(title = "活动分类")
    @Excel(name = "活动分类")
    private String category;

    /** 开始时间 */
    @Schema(title = "开始时间")
    @Excel(name = "开始时间")
    private Instant startTime;

    /** 结束时间 */
    @Schema(title = "结束时间")
    @Excel(name = "结束时间")
    private Instant endTime;

    /** 报名截止时间 */
    @Schema(title = "报名截止时间")
    @Excel(name = "报名截止时间")
    private Instant joinDeadline;

    /** 最大参与人数 */
    @Schema(title = "最大参与人数")
    @Excel(name = "最大参与人数")
    private Integer maxParticipants;

    /** 已报名人数 */
    @Schema(title = "已报名人数")
    @Excel(name = "已报名人数")
    private Integer registeredCount;

    /** 已签到人数 */
    @Schema(title = "已签到人数")
    @Excel(name = "已签到人数")
    private Integer checkedInCount;

    /** 乐观锁版本号 */
    @Schema(title = "乐观锁版本号")
    @Excel(name = "乐观锁版本号")
    private Integer version;

    /** 地点简述 */
    @Schema(title = "地点简述")
    @Excel(name = "地点简述")
    private String location;

    /** 会议/直播链接 */
    @Schema(title = "会议/直播链接")
    @Excel(name = "会议/直播链接")
    private String meetingUrl;

    /** 详细地址 */
    @Schema(title = "详细地址")
    @Excel(name = "详细地址")
    private String address;

    /** 城市 */
    @Schema(title = "城市")
    @Excel(name = "城市")
    private String city;

    /** 封面图 */
    @Schema(title = "封面图")
    @Excel(name = "封面图")
    private String coverImageUrl;

    /** 列表图 */
    @Schema(title = "列表图")
    @Excel(name = "列表图")
    private String listImageUrl;

    /** 详情图 */
    @Schema(title = "详情图")
    @Excel(name = "详情图")
    private String detailImageUrl;

    /** 海报图 */
    @Schema(title = "海报图")
    @Excel(name = "海报图")
    private String posterImageUrl;

    /** 状态：草稿/已发布/报名中/已满/进行中/已结束/已取消 */
    @Schema(title = "状态：草稿/已发布/报名中/已满/进行中/已结束/已取消")
    @Excel(name = "状态：草稿/已发布/报名中/已满/进行中/已结束/已取消")
    private String bizStatus;

    /** 是否公开可见 */
    @Schema(title = "是否公开可见")
    @Excel(name = "是否公开可见")
    private Boolean isPublic;

    /** 参与奖励积分 */
    @Schema(title = "参与奖励积分")
    @Excel(name = "参与奖励积分")
    private Integer basePointsReward;

    /** RRule周期规则 */
    @Schema(title = "RRule周期规则")
    @Excel(name = "RRule周期规则")
    private String recurrenceRule;

    /** 是否周期活动 */
    @Schema(title = "是否周期活动")
    @Excel(name = "是否周期活动")
    private Boolean isRecurring;

    /** 父活动ID(周期活动) */
    @Schema(title = "父活动ID(周期活动)")
    @Excel(name = "父活动ID(周期活动)")
    private Long parentEventId;

    /** 活动价格 */
    @Schema(title = "活动价格")
    @Excel(name = "活动价格")
    private BigDecimal eventPrice;

    /** 活动类型 */
    @Schema(title = "活动类型")
    @Excel(name = "活动类型")
    private String eventType;

    /** 目标学员画像 */
    @Schema(title = "目标学员画像")
    @Excel(name = "目标学员画像")
    private String targetAudience;

    /** 学习目标 */
    @Schema(title = "学习目标")
    @Excel(name = "学习目标")
    private String learningObjectives;

    /** 活动结构 */
    @Schema(title = "活动结构")
    @Excel(name = "活动结构")
    private String eventStructure;

    /** 活动内容大纲 */
    @Schema(title = "活动内容大纲")
    @Excel(name = "活动内容大纲")
    private String curriculum;

    /** 转化策略 */
    @Schema(title = "转化策略")
    @Excel(name = "转化策略")
    private String conversionStrategy;

    /** 跟进计划 */
    @Schema(title = "跟进计划")
    @Excel(name = "跟进计划")
    private String followUpPlan;

    /** 风险管理 */
    @Schema(title = "风险管理")
    @Excel(name = "风险管理")
    private String riskManagement;

    /** 活动标签 */
    @Schema(title = "活动标签")
    @Excel(name = "活动标签")
    private String eventTags;

    /** 主办方 */
    @Schema(title = "主办方")
    @Excel(name = "主办方")
    private String organizer;

    /** 联系方式 */
    @Schema(title = "联系方式")
    @Excel(name = "联系方式")
    private String contact;

    /** 负责人ID */
    @Schema(title = "负责人ID")
    @Excel(name = "负责人ID")
    private Long pmUserId;

    /** 删除人ID */
    @Schema(title = "删除人ID")
    private Long deleteId;

    /** 删除时间 */
    @Schema(title = "删除时间")
    private Instant deleteTime;

    /** 删除标志（0代表存在 1代表删除） */
    @Schema(title = "删除标志（0代表存在 1代表删除）")
    private Integer delFlag;

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

    /** 扩展配置 */
    @Schema(title = "扩展配置")
    @Excel(name = "扩展配置")
    private String jsonData;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
