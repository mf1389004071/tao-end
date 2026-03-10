package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.XiaoeOrders;
import com.geek.tao.bt10.mapper.XiaoeOrdersMapper;
import com.geek.tao.bt10.service.IXiaoeOrdersService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 小鹅通订单同步表 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class XiaoeOrdersServiceImpl extends ServiceImpl<XiaoeOrdersMapper, XiaoeOrders> implements IXiaoeOrdersService {

    private QueryChain<XiaoeOrders> selectList(XiaoeOrders xiaoeOrders) {
        QueryChain<XiaoeOrders> chain = this.queryChain();
        if (xiaoeOrders.getXiaoeOrderNo() != null && !xiaoeOrders.getXiaoeOrderNo().isEmpty()) {
            chain.eq(XiaoeOrders::getXiaoeOrderNo, xiaoeOrders.getXiaoeOrderNo());
        }
        if (xiaoeOrders.getUserId() != null) {
            chain.eq(XiaoeOrders::getUserId, xiaoeOrders.getUserId());
        }
        if (xiaoeOrders.getOrderState() != null && !xiaoeOrders.getOrderState().isEmpty()) {
            chain.eq(XiaoeOrders::getOrderState, xiaoeOrders.getOrderState());
        }
        if (xiaoeOrders.getActualFee() != null) {
            chain.eq(XiaoeOrders::getActualFee, xiaoeOrders.getActualFee());
        }
        if (xiaoeOrders.getGoodsName() != null && !xiaoeOrders.getGoodsName().isEmpty()) {
            chain.like(XiaoeOrders::getGoodsName, xiaoeOrders.getGoodsName());
        }
        if (xiaoeOrders.getGoodsType() != null && !xiaoeOrders.getGoodsType().isEmpty()) {
            chain.eq(XiaoeOrders::getGoodsType, xiaoeOrders.getGoodsType());
        }
        if (xiaoeOrders.getSpuType() != null && !xiaoeOrders.getSpuType().isEmpty()) {
            chain.eq(XiaoeOrders::getSpuType, xiaoeOrders.getSpuType());
        }
        if (xiaoeOrders.getPayState() != null && !xiaoeOrders.getPayState().isEmpty()) {
            chain.eq(XiaoeOrders::getPayState, xiaoeOrders.getPayState());
        }
        if (xiaoeOrders.getPayType() != null && !xiaoeOrders.getPayType().isEmpty()) {
            chain.eq(XiaoeOrders::getPayType, xiaoeOrders.getPayType());
        }
        if (xiaoeOrders.getTradeNo() != null && !xiaoeOrders.getTradeNo().isEmpty()) {
            chain.eq(XiaoeOrders::getTradeNo, xiaoeOrders.getTradeNo());
        }
        if (xiaoeOrders.getXiaoeCreateTime() != null) {
            chain.eq(XiaoeOrders::getXiaoeCreateTime, xiaoeOrders.getXiaoeCreateTime());
        }
        if (xiaoeOrders.getStudentInfo() != null && !xiaoeOrders.getStudentInfo().isEmpty()) {
            chain.eq(XiaoeOrders::getStudentInfo, xiaoeOrders.getStudentInfo());
        }
        if (xiaoeOrders.getInvoiceInfo() != null && !xiaoeOrders.getInvoiceInfo().isEmpty()) {
            chain.eq(XiaoeOrders::getInvoiceInfo, xiaoeOrders.getInvoiceInfo());
        }
        if (xiaoeOrders.getSyncStatus() != null && !xiaoeOrders.getSyncStatus().isEmpty()) {
            chain.eq(XiaoeOrders::getSyncStatus, xiaoeOrders.getSyncStatus());
        }
        if (xiaoeOrders.getLastSyncTime() != null) {
            chain.eq(XiaoeOrders::getLastSyncTime, xiaoeOrders.getLastSyncTime());
        }
        if (xiaoeOrders.getOrderNo() != null && !xiaoeOrders.getOrderNo().isEmpty()) {
            chain.eq(XiaoeOrders::getOrderNo, xiaoeOrders.getOrderNo());
        }
        if (xiaoeOrders.getProcessStatus() != null && !xiaoeOrders.getProcessStatus().isEmpty()) {
            chain.eq(XiaoeOrders::getProcessStatus, xiaoeOrders.getProcessStatus());
        }
        if (xiaoeOrders.getText1() != null && !xiaoeOrders.getText1().isEmpty()) {
            chain.eq(XiaoeOrders::getText1, xiaoeOrders.getText1());
        }
        if (xiaoeOrders.getText2() != null && !xiaoeOrders.getText2().isEmpty()) {
            chain.eq(XiaoeOrders::getText2, xiaoeOrders.getText2());
        }
        if (xiaoeOrders.getText3() != null && !xiaoeOrders.getText3().isEmpty()) {
            chain.eq(XiaoeOrders::getText3, xiaoeOrders.getText3());
        }
        if (xiaoeOrders.getJsonData() != null && !xiaoeOrders.getJsonData().isEmpty()) {
            chain.eq(XiaoeOrders::getJsonData, xiaoeOrders.getJsonData());
        }
        if (xiaoeOrders.getStatus() != null && !xiaoeOrders.getStatus().isEmpty()) {
            chain.eq(XiaoeOrders::getStatus, xiaoeOrders.getStatus());
        }
        return chain;
    }

    @Override
    public Page<XiaoeOrders> page(XiaoeOrders xiaoeOrders, int pageNum, int pageSize) {
        return selectList(xiaoeOrders).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(XiaoeOrders xiaoeOrders, HttpServletResponse response) {
        List<XiaoeOrders> list = selectList(xiaoeOrders).list();
        ExcelUtil<XiaoeOrders> util = new ExcelUtil<>(XiaoeOrders.class);
        util.exportExcel(response, list, "小鹅通订单同步表数据");
    }


}
