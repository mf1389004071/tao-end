package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.PaymentItems;
import com.geek.tao.bt10.mapper.PaymentItemsMapper;
import com.geek.tao.bt10.service.IPaymentItemsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 统一支付订单明细表 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class PaymentItemsServiceImpl extends ServiceImpl<PaymentItemsMapper, PaymentItems> implements IPaymentItemsService {

    private QueryChain<PaymentItems> selectList(PaymentItems paymentItems) {
        QueryChain<PaymentItems> chain = this.queryChain();
        if (paymentItems.getPaymentId() != null) {
            chain.eq(PaymentItems::getPaymentId, paymentItems.getPaymentId());
        }
        if (paymentItems.getItemType() != null && !paymentItems.getItemType().isEmpty()) {
            chain.eq(PaymentItems::getItemType, paymentItems.getItemType());
        }
        if (paymentItems.getItemName() != null && !paymentItems.getItemName().isEmpty()) {
            chain.like(PaymentItems::getItemName, paymentItems.getItemName());
        }
        if (paymentItems.getOriginalAmount() != null) {
            chain.eq(PaymentItems::getOriginalAmount, paymentItems.getOriginalAmount());
        }
        if (paymentItems.getAmount() != null) {
            chain.eq(PaymentItems::getAmount, paymentItems.getAmount());
        }
        if (paymentItems.getQuantity() != null) {
            chain.eq(PaymentItems::getQuantity, paymentItems.getQuantity());
        }
        if (paymentItems.getIsGift() != null) {
            chain.eq(PaymentItems::getIsGift, paymentItems.getIsGift());
        }
        if (paymentItems.getIsTransferable() != null) {
            chain.eq(PaymentItems::getIsTransferable, paymentItems.getIsTransferable());
        }
        if (paymentItems.getCanBuyForOthers() != null) {
            chain.eq(PaymentItems::getCanBuyForOthers, paymentItems.getCanBuyForOthers());
        }
        if (paymentItems.getUsageStatus() != null && !paymentItems.getUsageStatus().isEmpty()) {
            chain.eq(PaymentItems::getUsageStatus, paymentItems.getUsageStatus());
        }
        if (paymentItems.getGrantMethod() != null && !paymentItems.getGrantMethod().isEmpty()) {
            chain.eq(PaymentItems::getGrantMethod, paymentItems.getGrantMethod());
        }
        if (paymentItems.getRelatedType() != null && !paymentItems.getRelatedType().isEmpty()) {
            chain.eq(PaymentItems::getRelatedType, paymentItems.getRelatedType());
        }
        if (paymentItems.getRelatedId() != null) {
            chain.eq(PaymentItems::getRelatedId, paymentItems.getRelatedId());
        }
        if (paymentItems.getJsonData() != null && !paymentItems.getJsonData().isEmpty()) {
            chain.eq(PaymentItems::getJsonData, paymentItems.getJsonData());
        }
        if (paymentItems.getStatus() != null && !paymentItems.getStatus().isEmpty()) {
            chain.eq(PaymentItems::getStatus, paymentItems.getStatus());
        }
        return chain;
    }

    @Override
    public Page<PaymentItems> page(PaymentItems paymentItems, int pageNum, int pageSize) {
        return selectList(paymentItems).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(PaymentItems paymentItems, HttpServletResponse response) {
        List<PaymentItems> list = selectList(paymentItems).list();
        ExcelUtil<PaymentItems> util = new ExcelUtil<>(PaymentItems.class);
        util.exportExcel(response, list, "统一支付订单明细表数据");
    }


}
