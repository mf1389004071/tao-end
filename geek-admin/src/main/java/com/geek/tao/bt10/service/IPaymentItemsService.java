package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.PaymentItems;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 统一支付订单明细表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IPaymentItemsService extends IService<PaymentItems> {

    /**
     * 分页查询统一支付订单明细表
     *
     * @param paymentItems 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<PaymentItems> page(PaymentItems paymentItems, int pageNum, int pageSize);

    /**
     * 导出统一支付订单明细表
     *
     * @param paymentItems 查询条件
     * @param response 响应
     */
    void export(PaymentItems paymentItems, HttpServletResponse response);


}
