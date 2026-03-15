package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserProfiles;
import com.geek.tao.bt10.mapper.UserProfilesMapper;
import com.geek.tao.bt10.service.IUserProfilesService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户信息画像扩展表 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserProfilesServiceImpl extends ServiceImpl<UserProfilesMapper, UserProfiles> implements IUserProfilesService {

    private QueryChain<UserProfiles> selectList(UserProfiles userProfiles) {
        QueryChain<UserProfiles> chain = this.queryChain();
        if (userProfiles.getRealName() != null && !userProfiles.getRealName().isEmpty()) {
            chain.like(UserProfiles::getRealName, userProfiles.getRealName());
        }
        if (userProfiles.getPoints() != null) {
            chain.eq(UserProfiles::getPoints, userProfiles.getPoints());
        }
        if (userProfiles.getTotalPoints() != null) {
            chain.eq(UserProfiles::getTotalPoints, userProfiles.getTotalPoints());
        }
        if (userProfiles.getContributionPoints() != null) {
            chain.eq(UserProfiles::getContributionPoints, userProfiles.getContributionPoints());
        }
        if (userProfiles.getTotalContributionPoints() != null) {
            chain.eq(UserProfiles::getTotalContributionPoints, userProfiles.getTotalContributionPoints());
        }
        if (userProfiles.getGrowthStage() != null && !userProfiles.getGrowthStage().isEmpty()) {
            chain.eq(UserProfiles::getGrowthStage, userProfiles.getGrowthStage());
        }
        if (userProfiles.getJoinDate() != null) {
            chain.eq(UserProfiles::getJoinDate, userProfiles.getJoinDate());
        }
        if (userProfiles.getLastActiveAt() != null) {
            chain.eq(UserProfiles::getLastActiveAt, userProfiles.getLastActiveAt());
        }
        if (userProfiles.getWechatUnionid() != null && !userProfiles.getWechatUnionid().isEmpty()) {
            chain.eq(UserProfiles::getWechatUnionid, userProfiles.getWechatUnionid());
        }
        if (userProfiles.getWechatOpenid() != null && !userProfiles.getWechatOpenid().isEmpty()) {
            chain.eq(UserProfiles::getWechatOpenid, userProfiles.getWechatOpenid());
        }
        if (userProfiles.getMiniappOpenid() != null && !userProfiles.getMiniappOpenid().isEmpty()) {
            chain.eq(UserProfiles::getMiniappOpenid, userProfiles.getMiniappOpenid());
        }
        if (userProfiles.getWxWorkUserid() != null && !userProfiles.getWxWorkUserid().isEmpty()) {
            chain.eq(UserProfiles::getWxWorkUserid, userProfiles.getWxWorkUserid());
        }
        if (userProfiles.getXiaoeUserId() != null && !userProfiles.getXiaoeUserId().isEmpty()) {
            chain.eq(UserProfiles::getXiaoeUserId, userProfiles.getXiaoeUserId());
        }
        if (userProfiles.getXiaoeData() != null && !userProfiles.getXiaoeData().isEmpty()) {
            chain.eq(UserProfiles::getXiaoeData, userProfiles.getXiaoeData());
        }
        if (userProfiles.getInviterId() != null) {
            chain.eq(UserProfiles::getInviterId, userProfiles.getInviterId());
        }
        if (userProfiles.getInvitationCode() != null && !userProfiles.getInvitationCode().isEmpty()) {
            chain.eq(UserProfiles::getInvitationCode, userProfiles.getInvitationCode());
        }
        if (userProfiles.getBizRole() != null && !userProfiles.getBizRole().isEmpty()) {
            chain.eq(UserProfiles::getBizRole, userProfiles.getBizRole());
        }
        if (userProfiles.getBestPhotoUrl() != null && !userProfiles.getBestPhotoUrl().isEmpty()) {
            chain.eq(UserProfiles::getBestPhotoUrl, userProfiles.getBestPhotoUrl());
        }
        if (userProfiles.getAvatarWechatUrl() != null && !userProfiles.getAvatarWechatUrl().isEmpty()) {
            chain.eq(UserProfiles::getAvatarWechatUrl, userProfiles.getAvatarWechatUrl());
        }
        if (userProfiles.getAvatarPromoUrl() != null && !userProfiles.getAvatarPromoUrl().isEmpty()) {
            chain.eq(UserProfiles::getAvatarPromoUrl, userProfiles.getAvatarPromoUrl());
        }
        if (userProfiles.getPromoImageUrl() != null && !userProfiles.getPromoImageUrl().isEmpty()) {
            chain.eq(UserProfiles::getPromoImageUrl, userProfiles.getPromoImageUrl());
        }
        if (userProfiles.getHeartTreeUrl() != null && !userProfiles.getHeartTreeUrl().isEmpty()) {
            chain.eq(UserProfiles::getHeartTreeUrl, userProfiles.getHeartTreeUrl());
        }
        if (userProfiles.getHeartKeyUrl() != null && !userProfiles.getHeartKeyUrl().isEmpty()) {
            chain.eq(UserProfiles::getHeartKeyUrl, userProfiles.getHeartKeyUrl());
        }
        if (userProfiles.getBusinessPositioning() != null && !userProfiles.getBusinessPositioning().isEmpty()) {
            chain.eq(UserProfiles::getBusinessPositioning, userProfiles.getBusinessPositioning());
        }
        if (userProfiles.getTalentSummary() != null && !userProfiles.getTalentSummary().isEmpty()) {
            chain.eq(UserProfiles::getTalentSummary, userProfiles.getTalentSummary());
        }
        if (userProfiles.getAvailableTimeSlots() != null && !userProfiles.getAvailableTimeSlots().isEmpty()) {
            chain.eq(UserProfiles::getAvailableTimeSlots, userProfiles.getAvailableTimeSlots());
        }
        if (userProfiles.getAppointmentStatus() != null && !userProfiles.getAppointmentStatus().isEmpty()) {
            chain.eq(UserProfiles::getAppointmentStatus, userProfiles.getAppointmentStatus());
        }
        if (userProfiles.getExtraProfile() != null && !userProfiles.getExtraProfile().isEmpty()) {
            chain.eq(UserProfiles::getExtraProfile, userProfiles.getExtraProfile());
        }
        if (userProfiles.getText1() != null && !userProfiles.getText1().isEmpty()) {
            chain.eq(UserProfiles::getText1, userProfiles.getText1());
        }
        if (userProfiles.getText2() != null && !userProfiles.getText2().isEmpty()) {
            chain.eq(UserProfiles::getText2, userProfiles.getText2());
        }
        if (userProfiles.getText3() != null && !userProfiles.getText3().isEmpty()) {
            chain.eq(UserProfiles::getText3, userProfiles.getText3());
        }
        if (userProfiles.getJsonData() != null && !userProfiles.getJsonData().isEmpty()) {
            chain.eq(UserProfiles::getJsonData, userProfiles.getJsonData());
        }
        if (userProfiles.getStatus() != null && !userProfiles.getStatus().isEmpty()) {
            chain.eq(UserProfiles::getStatus, userProfiles.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserProfiles> page(UserProfiles userProfiles, int pageNum, int pageSize) {
        return selectList(userProfiles).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserProfiles userProfiles, HttpServletResponse response) {
        List<UserProfiles> list = selectList(userProfiles).list();
        ExcelUtil<UserProfiles> util = new ExcelUtil<>(UserProfiles.class);
        util.exportExcel(response, list, "用户信息画像扩展表数据");
    }


}
