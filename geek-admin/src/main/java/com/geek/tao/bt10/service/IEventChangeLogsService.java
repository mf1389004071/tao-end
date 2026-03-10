package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventChangeLogs;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动关键信息变更记录 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventChangeLogsService extends IService<EventChangeLogs> {

    /**
     * 分页查询活动关键信息变更记录
     *
     * @param eventChangeLogs 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventChangeLogs> page(EventChangeLogs eventChangeLogs, int pageNum, int pageSize);

    /**
     * 导出活动关键信息变更记录
     *
     * @param eventChangeLogs 查询条件
     * @param response 响应
     */
    void export(EventChangeLogs eventChangeLogs, HttpServletResponse response);


}
