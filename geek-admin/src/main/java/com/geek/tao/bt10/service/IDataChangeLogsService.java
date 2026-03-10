package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.DataChangeLogs;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 通用业务数据变更审计 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IDataChangeLogsService extends IService<DataChangeLogs> {

    /**
     * 分页查询通用业务数据变更审计
     *
     * @param dataChangeLogs 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<DataChangeLogs> page(DataChangeLogs dataChangeLogs, int pageNum, int pageSize);

    /**
     * 导出通用业务数据变更审计
     *
     * @param dataChangeLogs 查询条件
     * @param response 响应
     */
    void export(DataChangeLogs dataChangeLogs, HttpServletResponse response);


}
