package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventInfo;
import com.geek.tao.bt10.mapper.EventInfoMapper;
import com.geek.tao.bt10.service.IEventInfoService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动或线下课程主表 服务层实现
 * 负责人昵称通过 EventInfo 上的 @RelationManyToOne(pmUserId -> sys_user.nickName) 由 WithRelations 查询自动填充。
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventInfoServiceImpl extends ServiceImpl<EventInfoMapper, EventInfo> implements IEventInfoService {

    private QueryChain<EventInfo> selectList(EventInfo eventInfo) {
        QueryChain<EventInfo> chain = this.queryChain();
        if (eventInfo.getTitle() != null && !eventInfo.getTitle().isEmpty()) {
            chain.eq(EventInfo::getTitle, eventInfo.getTitle());
        }
        if (eventInfo.getJoinType() != null && !eventInfo.getJoinType().isEmpty()) {
            chain.eq(EventInfo::getJoinType, eventInfo.getJoinType());
        }
        if (eventInfo.getCategory() != null && !eventInfo.getCategory().isEmpty()) {
            chain.eq(EventInfo::getCategory, eventInfo.getCategory());
        }
        if (eventInfo.getStartTime() != null) {
            chain.eq(EventInfo::getStartTime, eventInfo.getStartTime());
        }
        if (eventInfo.getEndTime() != null) {
            chain.eq(EventInfo::getEndTime, eventInfo.getEndTime());
        }
        if (eventInfo.getJoinDeadline() != null) {
            chain.eq(EventInfo::getJoinDeadline, eventInfo.getJoinDeadline());
        }
        if (eventInfo.getMaxParticipants() != null) {
            chain.eq(EventInfo::getMaxParticipants, eventInfo.getMaxParticipants());
        }
        if (eventInfo.getRegisteredCount() != null) {
            chain.eq(EventInfo::getRegisteredCount, eventInfo.getRegisteredCount());
        }
        if (eventInfo.getCheckedInCount() != null) {
            chain.eq(EventInfo::getCheckedInCount, eventInfo.getCheckedInCount());
        }
        if (eventInfo.getVersion() != null) {
            chain.eq(EventInfo::getVersion, eventInfo.getVersion());
        }
        if (eventInfo.getLocation() != null && !eventInfo.getLocation().isEmpty()) {
            chain.eq(EventInfo::getLocation, eventInfo.getLocation());
        }
        if (eventInfo.getMeetingUrl() != null && !eventInfo.getMeetingUrl().isEmpty()) {
            chain.eq(EventInfo::getMeetingUrl, eventInfo.getMeetingUrl());
        }
        if (eventInfo.getAddress() != null && !eventInfo.getAddress().isEmpty()) {
            chain.eq(EventInfo::getAddress, eventInfo.getAddress());
        }
        if (eventInfo.getCity() != null && !eventInfo.getCity().isEmpty()) {
            chain.eq(EventInfo::getCity, eventInfo.getCity());
        }
        if (eventInfo.getCoverImageUrl() != null && !eventInfo.getCoverImageUrl().isEmpty()) {
            chain.eq(EventInfo::getCoverImageUrl, eventInfo.getCoverImageUrl());
        }
        if (eventInfo.getListImageUrl() != null && !eventInfo.getListImageUrl().isEmpty()) {
            chain.eq(EventInfo::getListImageUrl, eventInfo.getListImageUrl());
        }
        if (eventInfo.getDetailImageUrl() != null && !eventInfo.getDetailImageUrl().isEmpty()) {
            chain.eq(EventInfo::getDetailImageUrl, eventInfo.getDetailImageUrl());
        }
        if (eventInfo.getPosterImageUrl() != null && !eventInfo.getPosterImageUrl().isEmpty()) {
            chain.eq(EventInfo::getPosterImageUrl, eventInfo.getPosterImageUrl());
        }
        if (eventInfo.getBizStatus() != null && !eventInfo.getBizStatus().isEmpty()) {
            chain.eq(EventInfo::getBizStatus, eventInfo.getBizStatus());
        }
        if (eventInfo.getIsPublic() != null) {
            chain.eq(EventInfo::getIsPublic, eventInfo.getIsPublic());
        }
        if (eventInfo.getBasePointsReward() != null) {
            chain.eq(EventInfo::getBasePointsReward, eventInfo.getBasePointsReward());
        }
        if (eventInfo.getRecurrenceRule() != null && !eventInfo.getRecurrenceRule().isEmpty()) {
            chain.eq(EventInfo::getRecurrenceRule, eventInfo.getRecurrenceRule());
        }
        if (eventInfo.getIsRecurring() != null) {
            chain.eq(EventInfo::getIsRecurring, eventInfo.getIsRecurring());
        }
        if (eventInfo.getParentEventId() != null) {
            chain.eq(EventInfo::getParentEventId, eventInfo.getParentEventId());
        }
        if (eventInfo.getEventPrice() != null) {
            chain.eq(EventInfo::getEventPrice, eventInfo.getEventPrice());
        }
        if (eventInfo.getEventType() != null && !eventInfo.getEventType().isEmpty()) {
            chain.eq(EventInfo::getEventType, eventInfo.getEventType());
        }
        if (eventInfo.getTargetAudience() != null && !eventInfo.getTargetAudience().isEmpty()) {
            chain.eq(EventInfo::getTargetAudience, eventInfo.getTargetAudience());
        }
        if (eventInfo.getLearningObjectives() != null && !eventInfo.getLearningObjectives().isEmpty()) {
            chain.eq(EventInfo::getLearningObjectives, eventInfo.getLearningObjectives());
        }
        if (eventInfo.getEventStructure() != null && !eventInfo.getEventStructure().isEmpty()) {
            chain.eq(EventInfo::getEventStructure, eventInfo.getEventStructure());
        }
        if (eventInfo.getCurriculum() != null && !eventInfo.getCurriculum().isEmpty()) {
            chain.eq(EventInfo::getCurriculum, eventInfo.getCurriculum());
        }
        if (eventInfo.getConversionStrategy() != null && !eventInfo.getConversionStrategy().isEmpty()) {
            chain.eq(EventInfo::getConversionStrategy, eventInfo.getConversionStrategy());
        }
        if (eventInfo.getFollowUpPlan() != null && !eventInfo.getFollowUpPlan().isEmpty()) {
            chain.eq(EventInfo::getFollowUpPlan, eventInfo.getFollowUpPlan());
        }
        if (eventInfo.getRiskManagement() != null && !eventInfo.getRiskManagement().isEmpty()) {
            chain.eq(EventInfo::getRiskManagement, eventInfo.getRiskManagement());
        }
        if (eventInfo.getEventTags() != null && !eventInfo.getEventTags().isEmpty()) {
            chain.eq(EventInfo::getEventTags, eventInfo.getEventTags());
        }
        if (eventInfo.getOrganizer() != null && !eventInfo.getOrganizer().isEmpty()) {
            chain.eq(EventInfo::getOrganizer, eventInfo.getOrganizer());
        }
        if (eventInfo.getContact() != null && !eventInfo.getContact().isEmpty()) {
            chain.eq(EventInfo::getContact, eventInfo.getContact());
        }
        if (eventInfo.getPmUserId() != null) {
            chain.eq(EventInfo::getPmUserId, eventInfo.getPmUserId());
        }
        if (eventInfo.getText1() != null && !eventInfo.getText1().isEmpty()) {
            chain.eq(EventInfo::getText1, eventInfo.getText1());
        }
        if (eventInfo.getText2() != null && !eventInfo.getText2().isEmpty()) {
            chain.eq(EventInfo::getText2, eventInfo.getText2());
        }
        if (eventInfo.getText3() != null && !eventInfo.getText3().isEmpty()) {
            chain.eq(EventInfo::getText3, eventInfo.getText3());
        }
        if (eventInfo.getJsonData() != null && !eventInfo.getJsonData().isEmpty()) {
            chain.eq(EventInfo::getJsonData, eventInfo.getJsonData());
        }
        if (eventInfo.getStatus() != null && !eventInfo.getStatus().isEmpty()) {
            chain.eq(EventInfo::getStatus, eventInfo.getStatus());
        }
        return chain;
    }

    /** 构建与 selectList 条件一致的 QueryWrapper，供 WithRelations 查询使用 */
    private QueryWrapper buildQueryWrapper(EventInfo eventInfo) {
        QueryWrapper qw = QueryWrapper.create().from(EventInfo.class);
        if (eventInfo.getTitle() != null && !eventInfo.getTitle().isEmpty()) {
            qw.and(EventInfo::getTitle).eq(eventInfo.getTitle());
        }
        if (eventInfo.getJoinType() != null && !eventInfo.getJoinType().isEmpty()) {
            qw.and(EventInfo::getJoinType).eq(eventInfo.getJoinType());
        }
        if (eventInfo.getCategory() != null && !eventInfo.getCategory().isEmpty()) {
            qw.and(EventInfo::getCategory).eq(eventInfo.getCategory());
        }
        if (eventInfo.getStartTime() != null) {
            qw.and(EventInfo::getStartTime).eq(eventInfo.getStartTime());
        }
        if (eventInfo.getEndTime() != null) {
            qw.and(EventInfo::getEndTime).eq(eventInfo.getEndTime());
        }
        if (eventInfo.getJoinDeadline() != null) {
            qw.and(EventInfo::getJoinDeadline).eq(eventInfo.getJoinDeadline());
        }
        if (eventInfo.getMaxParticipants() != null) {
            qw.and(EventInfo::getMaxParticipants).eq(eventInfo.getMaxParticipants());
        }
        if (eventInfo.getRegisteredCount() != null) {
            qw.and(EventInfo::getRegisteredCount).eq(eventInfo.getRegisteredCount());
        }
        if (eventInfo.getCheckedInCount() != null) {
            qw.and(EventInfo::getCheckedInCount).eq(eventInfo.getCheckedInCount());
        }
        if (eventInfo.getVersion() != null) {
            qw.and(EventInfo::getVersion).eq(eventInfo.getVersion());
        }
        if (eventInfo.getLocation() != null && !eventInfo.getLocation().isEmpty()) {
            qw.and(EventInfo::getLocation).eq(eventInfo.getLocation());
        }
        if (eventInfo.getMeetingUrl() != null && !eventInfo.getMeetingUrl().isEmpty()) {
            qw.and(EventInfo::getMeetingUrl).eq(eventInfo.getMeetingUrl());
        }
        if (eventInfo.getAddress() != null && !eventInfo.getAddress().isEmpty()) {
            qw.and(EventInfo::getAddress).eq(eventInfo.getAddress());
        }
        if (eventInfo.getCity() != null && !eventInfo.getCity().isEmpty()) {
            qw.and(EventInfo::getCity).eq(eventInfo.getCity());
        }
        if (eventInfo.getCoverImageUrl() != null && !eventInfo.getCoverImageUrl().isEmpty()) {
            qw.and(EventInfo::getCoverImageUrl).eq(eventInfo.getCoverImageUrl());
        }
        if (eventInfo.getListImageUrl() != null && !eventInfo.getListImageUrl().isEmpty()) {
            qw.and(EventInfo::getListImageUrl).eq(eventInfo.getListImageUrl());
        }
        if (eventInfo.getDetailImageUrl() != null && !eventInfo.getDetailImageUrl().isEmpty()) {
            qw.and(EventInfo::getDetailImageUrl).eq(eventInfo.getDetailImageUrl());
        }
        if (eventInfo.getPosterImageUrl() != null && !eventInfo.getPosterImageUrl().isEmpty()) {
            qw.and(EventInfo::getPosterImageUrl).eq(eventInfo.getPosterImageUrl());
        }
        if (eventInfo.getBizStatus() != null && !eventInfo.getBizStatus().isEmpty()) {
            qw.and(EventInfo::getBizStatus).eq(eventInfo.getBizStatus());
        }
        if (eventInfo.getIsPublic() != null) {
            qw.and(EventInfo::getIsPublic).eq(eventInfo.getIsPublic());
        }
        if (eventInfo.getBasePointsReward() != null) {
            qw.and(EventInfo::getBasePointsReward).eq(eventInfo.getBasePointsReward());
        }
        if (eventInfo.getRecurrenceRule() != null && !eventInfo.getRecurrenceRule().isEmpty()) {
            qw.and(EventInfo::getRecurrenceRule).eq(eventInfo.getRecurrenceRule());
        }
        if (eventInfo.getIsRecurring() != null) {
            qw.and(EventInfo::getIsRecurring).eq(eventInfo.getIsRecurring());
        }
        if (eventInfo.getParentEventId() != null) {
            qw.and(EventInfo::getParentEventId).eq(eventInfo.getParentEventId());
        }
        if (eventInfo.getEventPrice() != null) {
            qw.and(EventInfo::getEventPrice).eq(eventInfo.getEventPrice());
        }
        if (eventInfo.getEventType() != null && !eventInfo.getEventType().isEmpty()) {
            qw.and(EventInfo::getEventType).eq(eventInfo.getEventType());
        }
        if (eventInfo.getTargetAudience() != null && !eventInfo.getTargetAudience().isEmpty()) {
            qw.and(EventInfo::getTargetAudience).eq(eventInfo.getTargetAudience());
        }
        if (eventInfo.getLearningObjectives() != null && !eventInfo.getLearningObjectives().isEmpty()) {
            qw.and(EventInfo::getLearningObjectives).eq(eventInfo.getLearningObjectives());
        }
        if (eventInfo.getEventStructure() != null && !eventInfo.getEventStructure().isEmpty()) {
            qw.and(EventInfo::getEventStructure).eq(eventInfo.getEventStructure());
        }
        if (eventInfo.getCurriculum() != null && !eventInfo.getCurriculum().isEmpty()) {
            qw.and(EventInfo::getCurriculum).eq(eventInfo.getCurriculum());
        }
        if (eventInfo.getConversionStrategy() != null && !eventInfo.getConversionStrategy().isEmpty()) {
            qw.and(EventInfo::getConversionStrategy).eq(eventInfo.getConversionStrategy());
        }
        if (eventInfo.getFollowUpPlan() != null && !eventInfo.getFollowUpPlan().isEmpty()) {
            qw.and(EventInfo::getFollowUpPlan).eq(eventInfo.getFollowUpPlan());
        }
        if (eventInfo.getRiskManagement() != null && !eventInfo.getRiskManagement().isEmpty()) {
            qw.and(EventInfo::getRiskManagement).eq(eventInfo.getRiskManagement());
        }
        if (eventInfo.getEventTags() != null && !eventInfo.getEventTags().isEmpty()) {
            qw.and(EventInfo::getEventTags).eq(eventInfo.getEventTags());
        }
        if (eventInfo.getOrganizer() != null && !eventInfo.getOrganizer().isEmpty()) {
            qw.and(EventInfo::getOrganizer).eq(eventInfo.getOrganizer());
        }
        if (eventInfo.getContact() != null && !eventInfo.getContact().isEmpty()) {
            qw.and(EventInfo::getContact).eq(eventInfo.getContact());
        }
        if (eventInfo.getPmUserId() != null) {
            qw.and(EventInfo::getPmUserId).eq(eventInfo.getPmUserId());
        }
        if (eventInfo.getText1() != null && !eventInfo.getText1().isEmpty()) {
            qw.and(EventInfo::getText1).eq(eventInfo.getText1());
        }
        if (eventInfo.getText2() != null && !eventInfo.getText2().isEmpty()) {
            qw.and(EventInfo::getText2).eq(eventInfo.getText2());
        }
        if (eventInfo.getText3() != null && !eventInfo.getText3().isEmpty()) {
            qw.and(EventInfo::getText3).eq(eventInfo.getText3());
        }
        if (eventInfo.getJsonData() != null && !eventInfo.getJsonData().isEmpty()) {
            qw.and(EventInfo::getJsonData).eq(eventInfo.getJsonData());
        }
        if (eventInfo.getStatus() != null && !eventInfo.getStatus().isEmpty()) {
            qw.and(EventInfo::getStatus).eq(eventInfo.getStatus());
        }
        // 默认按更新时间倒序（近期热门/列表页要求）
        qw.orderBy(EventInfo::getUpdateTime, false);
        return qw;
    }

    @Override
    public Page<EventInfo> page(EventInfo eventInfo, int pageNum, int pageSize) {
        return getMapper().paginateWithRelations(Page.of(pageNum, pageSize), buildQueryWrapper(eventInfo));
    }

    @Override
    public void export(EventInfo eventInfo, HttpServletResponse response) {
        List<EventInfo> list = getMapper().selectListWithRelationsByQuery(buildQueryWrapper(eventInfo));
        ExcelUtil<EventInfo> util = new ExcelUtil<>(EventInfo.class);
        util.exportExcel(response, list, "活动或线下课程主表数据");
    }

    @Override
    public EventInfo getInfo(Long id) {
        return getMapper().selectOneWithRelationsByQuery(
            QueryWrapper.create().from(EventInfo.class).eq(EventInfo::getId, id));
    }

}
