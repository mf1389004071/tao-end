package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserIdentities;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户身份关系表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserIdentitiesService extends IService<UserIdentities> {

    /**
     * 分页查询用户身份关系表
     *
     * @param userIdentities 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserIdentities> page(UserIdentities userIdentities, int pageNum, int pageSize);

    /**
     * 导出用户身份关系表
     *
     * @param userIdentities 查询条件
     * @param response 响应
     */
    void export(UserIdentities userIdentities, HttpServletResponse response);


}
