package com.geek.tao.bt10.service;

import java.util.Map;

/**
 * C 端邀请：新用户绑定邀请人，给邀请人身份域次数 +1。
 */
public interface ICustInviteService {

    /**
     * @param inviteeUserId 被邀请人（当前登录用户）
     * @param inviterId 邀请人 userId
     * @param identityCode 默认 IDENTITY_P6
     */
    Map<String, Object> bind(Long inviteeUserId, Long inviterId, String identityCode);
}
