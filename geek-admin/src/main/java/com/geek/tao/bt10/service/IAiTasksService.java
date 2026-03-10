package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.AiTasks;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * AI异步任务 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IAiTasksService extends IService<AiTasks> {

    /**
     * 分页查询AI异步任务
     *
     * @param aiTasks 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<AiTasks> page(AiTasks aiTasks, int pageNum, int pageSize);

    /**
     * 导出AI异步任务
     *
     * @param aiTasks 查询条件
     * @param response 响应
     */
    void export(AiTasks aiTasks, HttpServletResponse response);


}
