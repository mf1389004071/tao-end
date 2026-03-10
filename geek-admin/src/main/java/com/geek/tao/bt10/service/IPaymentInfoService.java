package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.PaymentInfo;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 统一支付订单 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IPaymentInfoService extends IService<PaymentInfo> {

    /**
     * 分页查询统一支付订单
     *
     * @param paymentInfo 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<PaymentInfo> page(PaymentInfo paymentInfo, int pageNum, int pageSize);

    /**
     * 导出统一支付订单
     *
     * @param paymentInfo 查询条件
     * @param response 响应
     */
    void export(PaymentInfo paymentInfo, HttpServletResponse response);


}
