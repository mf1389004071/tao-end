package com.geek.tao.bt10.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geek.common.core.domain.entity.SysUser;
import com.geek.system.service.ISysUserService;
import com.geek.tao.bt10.domain.UserProfiles;
import com.geek.tao.bt10.domain.UserSocialAction;
import com.geek.tao.bt10.service.ICustUserPublicService;
import com.geek.tao.bt10.service.IUserProfilesService;
import com.geek.tao.bt10.service.IUserSocialActionService;
/**
 * C 端用户公开主页业务实现
 */
@Service
public class CustUserPublicServiceImpl implements ICustUserPublicService {

    @Autowired
    private IUserProfilesService userProfilesService;
    @Autowired
    private IUserSocialActionService userSocialActionService;
    @Autowired
    private ISysUserService sysUserService;

    private UserProfiles ensureProfileRow(Long userId) {
        UserProfiles p = userProfilesService.getById(userId);
        if (p == null) {
            p = new UserProfiles();
            p.setUserId(userId);
            p.setProfileReceivedLikeCount(0L);
            p.setProfileReceivedCollectCount(0L);
            p.setProfileReceivedShareCount(0L);
            userProfilesService.save(p);
        }
        return p;
    }

    private static void bumpCount(UserProfiles p, String actionType, int delta) {
        if ("LIKE".equals(actionType)) {
            long v = (p.getProfileReceivedLikeCount() == null ? 0L : p.getProfileReceivedLikeCount()) + delta;
            p.setProfileReceivedLikeCount(Math.max(0, v));
        } else if ("COLLECT".equals(actionType)) {
            long v = (p.getProfileReceivedCollectCount() == null ? 0L : p.getProfileReceivedCollectCount()) + delta;
            p.setProfileReceivedCollectCount(Math.max(0, v));
        } else if ("SHARE".equals(actionType)) {
            long v = (p.getProfileReceivedShareCount() == null ? 0L : p.getProfileReceivedShareCount()) + delta;
            p.setProfileReceivedShareCount(Math.max(0, v));
        }
    }

    @Override
    public Map<String, Object> getPublicProfile(Long userId, Long viewerUserId) {
        SysUser u = sysUserService.selectUserById(userId);
        if (u == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        UserProfiles p = userProfilesService.getById(userId);
        boolean liked = false;
        boolean collected = false;
        if (viewerUserId != null && !viewerUserId.equals(userId)) {
            liked = userSocialActionService.queryChain()
                .eq(UserSocialAction::getTargetUserId, userId)
                .eq(UserSocialAction::getActorUserId, viewerUserId)
                .eq(UserSocialAction::getActionType, "LIKE")
                .one() != null;
            collected = userSocialActionService.queryChain()
                .eq(UserSocialAction::getTargetUserId, userId)
                .eq(UserSocialAction::getActorUserId, viewerUserId)
                .eq(UserSocialAction::getActionType, "COLLECT")
                .one() != null;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userId", String.valueOf(userId));
        data.put("nickName", u.getNickName() != null && !u.getNickName().isEmpty() ? u.getNickName() : u.getUserName());
        data.put("userName", u.getUserName());
        if (p != null) {
            data.put("realName", p.getRealName());
            data.put("growthStage", p.getGrowthStage());
            data.put("businessPositioning", p.getBusinessPositioning());
            data.put("talentSummary", p.getTalentSummary());
            data.put("bestPhotoUrl", p.getBestPhotoUrl());
            data.put("avatarPromoUrl", p.getAvatarPromoUrl());
            data.put("promoImageUrl", p.getPromoImageUrl());
            data.put("profileReceivedLikeCount", p.getProfileReceivedLikeCount() == null ? 0L : p.getProfileReceivedLikeCount());
            data.put("profileReceivedCollectCount", p.getProfileReceivedCollectCount() == null ? 0L : p.getProfileReceivedCollectCount());
            data.put("profileReceivedShareCount", p.getProfileReceivedShareCount() == null ? 0L : p.getProfileReceivedShareCount());
        } else {
            data.put("profileReceivedLikeCount", 0L);
            data.put("profileReceivedCollectCount", 0L);
            data.put("profileReceivedShareCount", 0L);
        }
        data.put("liked", liked);
        data.put("collected", collected);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> toggleSocial(Long targetUserId, Long actorUserId, String actionType, Boolean enabled) {
        if (targetUserId.equals(actorUserId)) {
            throw new IllegalArgumentException("不能对自己操作");
        }
        String at = actionType == null ? "" : actionType.trim().toUpperCase();
        if (!("LIKE".equals(at) || "COLLECT".equals(at))) {
            throw new IllegalArgumentException("actionType 不合法");
        }
        if (sysUserService.selectUserById(targetUserId) == null) {
            throw new IllegalArgumentException("目标用户不存在");
        }
        UserProfiles profile = ensureProfileRow(targetUserId);
        UserSocialAction exists = userSocialActionService.queryChain()
            .eq(UserSocialAction::getTargetUserId, targetUserId)
            .eq(UserSocialAction::getActorUserId, actorUserId)
            .eq(UserSocialAction::getActionType, at)
            .one();
        boolean nextEnabled = enabled != null ? enabled : (exists == null);
        if (nextEnabled && exists == null) {
            UserSocialAction dead = userSocialActionService.findIncludingDeleted(targetUserId, actorUserId, at);
            if (dead != null) {
                userSocialActionService.restoreIfSoftDeleted(dead);
            } else {
                UserSocialAction a = new UserSocialAction();
                a.setTargetUserId(targetUserId);
                a.setActorUserId(actorUserId);
                a.setActionType(at);
                userSocialActionService.save(a);
            }
            bumpCount(profile, at, +1);
            userProfilesService.updateById(profile);
        } else if (!nextEnabled && exists != null) {
            userSocialActionService.removeById(exists.getId());
            bumpCount(profile, at, -1);
            userProfilesService.updateById(profile);
        }
        UserProfiles fresh = userProfilesService.getById(targetUserId);
        UserSocialAction after = userSocialActionService.queryChain()
            .eq(UserSocialAction::getTargetUserId, targetUserId)
            .eq(UserSocialAction::getActorUserId, actorUserId)
            .eq(UserSocialAction::getActionType, at)
            .one();
        Map<String, Object> data = new HashMap<>();
        data.put("enabled", after != null);
        data.put("profileReceivedLikeCount", fresh != null && fresh.getProfileReceivedLikeCount() != null ? fresh.getProfileReceivedLikeCount() : 0L);
        data.put("profileReceivedCollectCount", fresh != null && fresh.getProfileReceivedCollectCount() != null ? fresh.getProfileReceivedCollectCount() : 0L);
        data.put("profileReceivedShareCount", fresh != null && fresh.getProfileReceivedShareCount() != null ? fresh.getProfileReceivedShareCount() : 0L);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> recordShare(Long targetUserId, Long actorUserId) {
        if (targetUserId.equals(actorUserId)) {
            throw new IllegalArgumentException("不能对自己操作");
        }
        if (sysUserService.selectUserById(targetUserId) == null) {
            throw new IllegalArgumentException("目标用户不存在");
        }
        UserProfiles profile = ensureProfileRow(targetUserId);
        bumpCount(profile, "SHARE", +1);
        userProfilesService.updateById(profile);
        UserSocialAction exists = userSocialActionService.queryChain()
            .eq(UserSocialAction::getTargetUserId, targetUserId)
            .eq(UserSocialAction::getActorUserId, actorUserId)
            .eq(UserSocialAction::getActionType, "SHARE")
            .one();
        if (exists == null) {
            UserSocialAction dead = userSocialActionService.findIncludingDeleted(targetUserId, actorUserId, "SHARE");
            if (dead != null) {
                userSocialActionService.restoreIfSoftDeleted(dead);
            } else {
                UserSocialAction a = new UserSocialAction();
                a.setTargetUserId(targetUserId);
                a.setActorUserId(actorUserId);
                a.setActionType("SHARE");
                userSocialActionService.save(a);
            }
        }
        UserProfiles fresh = userProfilesService.getById(targetUserId);
        Map<String, Object> data = new HashMap<>();
        data.put("profileReceivedShareCount", fresh != null && fresh.getProfileReceivedShareCount() != null ? fresh.getProfileReceivedShareCount() : 0L);
        return data;
    }
}
