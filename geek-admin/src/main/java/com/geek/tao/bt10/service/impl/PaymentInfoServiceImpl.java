package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.PaymentInfo;
import com.geek.tao.bt10.mapper.PaymentInfoMapper;
import com.geek.tao.bt10.service.IPaymentInfoService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 统一支付订单 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements IPaymentInfoService {

    private QueryChain<PaymentInfo> selectList(PaymentInfo paymentInfo) {
        QueryChain<PaymentInfo> chain = this.queryChain();
        if (paymentInfo.getOrderNo() != null && !paymentInfo.getOrderNo().isEmpty()) {
            chain.eq(PaymentInfo::getOrderNo, paymentInfo.getOrderNo());
        }
        if (paymentInfo.getUserId() != null) {
            chain.eq(PaymentInfo::getUserId, paymentInfo.getUserId());
        }
        if (paymentInfo.getOrderType() != null && !paymentInfo.getOrderType().isEmpty()) {
            chain.eq(PaymentInfo::getOrderType, paymentInfo.getOrderType());
        }
        if (paymentInfo.getRelatedType() != null && !paymentInfo.getRelatedType().isEmpty()) {
            chain.eq(PaymentInfo::getRelatedType, paymentInfo.getRelatedType());
        }
        if (paymentInfo.getRelatedId() != null) {
            chain.eq(PaymentInfo::getRelatedId, paymentInfo.getRelatedId());
        }
        if (paymentInfo.getTotalAmount() != null) {
            chain.eq(PaymentInfo::getTotalAmount, paymentInfo.getTotalAmount());
        }
        if (paymentInfo.getContribAmount() != null) {
            chain.eq(PaymentInfo::getContribAmount, paymentInfo.getContribAmount());
        }
        if (paymentInfo.getPointsAmount() != null) {
            chain.eq(PaymentInfo::getPointsAmount, paymentInfo.getPointsAmount());
        }
        if (paymentInfo.getCashAmount() != null) {
            chain.eq(PaymentInfo::getCashAmount, paymentInfo.getCashAmount());
        }
        if (paymentInfo.getPaymentStatus() != null && !paymentInfo.getPaymentStatus().isEmpty()) {
            chain.eq(PaymentInfo::getPaymentStatus, paymentInfo.getPaymentStatus());
        }
        if (paymentInfo.getPaymentMethod() != null && !paymentInfo.getPaymentMethod().isEmpty()) {
            chain.eq(PaymentInfo::getPaymentMethod, paymentInfo.getPaymentMethod());
        }
        if (paymentInfo.getPaymentNo() != null && !paymentInfo.getPaymentNo().isEmpty()) {
            chain.eq(PaymentInfo::getPaymentNo, paymentInfo.getPaymentNo());
        }
        if (paymentInfo.getPaidTime() != null) {
            chain.eq(PaymentInfo::getPaidTime, paymentInfo.getPaidTime());
        }
        if (paymentInfo.getRefundAmount() != null) {
            chain.eq(PaymentInfo::getRefundAmount, paymentInfo.getRefundAmount());
        }
        if (paymentInfo.getRefundReason() != null && !paymentInfo.getRefundReason().isEmpty()) {
            chain.eq(PaymentInfo::getRefundReason, paymentInfo.getRefundReason());
        }
        if (paymentInfo.getRefundedTime() != null) {
            chain.eq(PaymentInfo::getRefundedTime, paymentInfo.getRefundedTime());
        }
        if (paymentInfo.getText1() != null && !paymentInfo.getText1().isEmpty()) {
            chain.eq(PaymentInfo::getText1, paymentInfo.getText1());
        }
        if (paymentInfo.getText2() != null && !paymentInfo.getText2().isEmpty()) {
            chain.eq(PaymentInfo::getText2, paymentInfo.getText2());
        }
        if (paymentInfo.getText3() != null && !paymentInfo.getText3().isEmpty()) {
            chain.eq(PaymentInfo::getText3, paymentInfo.getText3());
        }
        if (paymentInfo.getJsonData() != null && !paymentInfo.getJsonData().isEmpty()) {
            chain.eq(PaymentInfo::getJsonData, paymentInfo.getJsonData());
        }
        if (paymentInfo.getStatus() != null && !paymentInfo.getStatus().isEmpty()) {
            chain.eq(PaymentInfo::getStatus, paymentInfo.getStatus());
        }
        return chain;
    }

    @Override
    public Page<PaymentInfo> page(PaymentInfo paymentInfo, int pageNum, int pageSize) {
        return selectList(paymentInfo).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(PaymentInfo paymentInfo, HttpServletResponse response) {
        List<PaymentInfo> list = selectList(paymentInfo).list();
        ExcelUtil<PaymentInfo> util = new ExcelUtil<>(PaymentInfo.class);
        util.exportExcel(response, list, "统一支付订单数据");
    }


}
