package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserContribLogs;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户贡献点收支流水 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserContribLogsService extends IService<UserContribLogs> {

    /**
     * 分页查询用户贡献点收支流水
     *
     * @param userContribLogs 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserContribLogs> page(UserContribLogs userContribLogs, int pageNum, int pageSize);

    /**
     * 导出用户贡献点收支流水
     *
     * @param userContribLogs 查询条件
     * @param response 响应
     */
    void export(UserContribLogs userContribLogs, HttpServletResponse response);


}
