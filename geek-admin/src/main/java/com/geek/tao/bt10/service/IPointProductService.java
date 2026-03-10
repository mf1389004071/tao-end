package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.PointProduct;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 积分商城商品 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IPointProductService extends IService<PointProduct> {

    /**
     * 分页查询积分商城商品
     *
     * @param pointProduct 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<PointProduct> page(PointProduct pointProduct, int pageNum, int pageSize);

    /**
     * 导出积分商城商品
     *
     * @param pointProduct 查询条件
     * @param response 响应
     */
    void export(PointProduct pointProduct, HttpServletResponse response);


}
