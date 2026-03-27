package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserSocialAction;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户主页社交动作 user_social_action 服务
 */
public interface IUserSocialActionService extends IService<UserSocialAction> {

    Page<UserSocialAction> page(UserSocialAction query, int pageNum, int pageSize);

    void export(UserSocialAction query, HttpServletResponse response);

    /**
     * 含逻辑删除行的查询（用于唯一键冲突时恢复）
     */
    UserSocialAction findIncludingDeleted(Long targetUserId, Long actorUserId, String actionType);

    void restoreIfSoftDeleted(UserSocialAction row);
}
