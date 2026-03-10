package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserProfiles;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户信息画像扩展表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserProfilesService extends IService<UserProfiles> {

    /**
     * 分页查询用户信息画像扩展表
     *
     * @param userProfiles 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserProfiles> page(UserProfiles userProfiles, int pageNum, int pageSize);

    /**
     * 导出用户信息画像扩展表
     *
     * @param userProfiles 查询条件
     * @param response 响应
     */
    void export(UserProfiles userProfiles, HttpServletResponse response);


}
