package com.geek.tao.bt10.service;

import java.util.Map;

/**
 * C 端用户公开主页（组合 user_profiles / user_social_action）
 */
public interface ICustUserPublicService {

    Map<String, Object> getPublicProfile(Long userId, Long viewerUserId);

    Map<String, Object> toggleSocial(Long targetUserId, Long actorUserId, String actionType, Boolean enabled);

    Map<String, Object> recordShare(Long targetUserId, Long actorUserId);
}
