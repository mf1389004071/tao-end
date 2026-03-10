package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserActivityLogs;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户行为轨迹日志 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserActivityLogsService extends IService<UserActivityLogs> {

    /**
     * 分页查询用户行为轨迹日志
     *
     * @param userActivityLogs 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserActivityLogs> page(UserActivityLogs userActivityLogs, int pageNum, int pageSize);

    /**
     * 导出用户行为轨迹日志
     *
     * @param userActivityLogs 查询条件
     * @param response 响应
     */
    void export(UserActivityLogs userActivityLogs, HttpServletResponse response);


}
