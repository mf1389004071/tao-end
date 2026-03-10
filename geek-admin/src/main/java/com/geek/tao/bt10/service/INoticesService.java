package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.Notices;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统级通知与公告 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface INoticesService extends IService<Notices> {

    /**
     * 分页查询系统级通知与公告
     *
     * @param notices 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<Notices> page(Notices notices, int pageNum, int pageSize);

    /**
     * 导出系统级通知与公告
     *
     * @param notices 查询条件
     * @param response 响应
     */
    void export(Notices notices, HttpServletResponse response);


}
