package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.XiaoeOrders;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 小鹅通订单同步表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IXiaoeOrdersService extends IService<XiaoeOrders> {

    /**
     * 分页查询小鹅通订单同步表
     *
     * @param xiaoeOrders 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<XiaoeOrders> page(XiaoeOrders xiaoeOrders, int pageNum, int pageSize);

    /**
     * 导出小鹅通订单同步表
     *
     * @param xiaoeOrders 查询条件
     * @param response 响应
     */
    void export(XiaoeOrders xiaoeOrders, HttpServletResponse response);


}
