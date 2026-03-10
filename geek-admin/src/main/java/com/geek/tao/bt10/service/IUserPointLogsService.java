package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserPointLogs;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户积分收支流水 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserPointLogsService extends IService<UserPointLogs> {

    /**
     * 分页查询用户积分收支流水
     *
     * @param userPointLogs 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserPointLogs> page(UserPointLogs userPointLogs, int pageNum, int pageSize);

    /**
     * 导出用户积分收支流水
     *
     * @param userPointLogs 查询条件
     * @param response 响应
     */
    void export(UserPointLogs userPointLogs, HttpServletResponse response);


}
