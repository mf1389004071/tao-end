package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserInvite;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 邀请关系与奖励记录 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserInviteService extends IService<UserInvite> {

    /**
     * 分页查询邀请关系与奖励记录
     *
     * @param userInvite 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserInvite> page(UserInvite userInvite, int pageNum, int pageSize);

    /**
     * 导出邀请关系与奖励记录
     *
     * @param userInvite 查询条件
     * @param response 响应
     */
    void export(UserInvite userInvite, HttpServletResponse response);


}
