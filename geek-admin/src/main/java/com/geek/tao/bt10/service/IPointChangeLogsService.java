package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.PointChangeLogs;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 积分变动审计 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IPointChangeLogsService extends IService<PointChangeLogs> {

    /**
     * 分页查询积分变动审计
     *
     * @param pointChangeLogs 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<PointChangeLogs> page(PointChangeLogs pointChangeLogs, int pageNum, int pageSize);

    /**
     * 导出积分变动审计
     *
     * @param pointChangeLogs 查询条件
     * @param response 响应
     */
    void export(PointChangeLogs pointChangeLogs, HttpServletResponse response);


}
