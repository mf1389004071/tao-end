package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserTags;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户与多维标签关联表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserTagsService extends IService<UserTags> {

    /**
     * 分页查询用户与多维标签关联表
     *
     * @param userTags 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserTags> page(UserTags userTags, int pageNum, int pageSize);

    /**
     * 导出用户与多维标签关联表
     *
     * @param userTags 查询条件
     * @param response 响应
     */
    void export(UserTags userTags, HttpServletResponse response);


}
