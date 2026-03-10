package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.PointProduct;
import com.geek.tao.bt10.mapper.PointProductMapper;
import com.geek.tao.bt10.service.IPointProductService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 积分商城商品 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class PointProductServiceImpl extends ServiceImpl<PointProductMapper, PointProduct> implements IPointProductService {

    private QueryChain<PointProduct> selectList(PointProduct pointProduct) {
        QueryChain<PointProduct> chain = this.queryChain();
        if (pointProduct.getName() != null && !pointProduct.getName().isEmpty()) {
            chain.like(PointProduct::getName, pointProduct.getName());
        }
        if (pointProduct.getProductType() != null && !pointProduct.getProductType().isEmpty()) {
            chain.eq(PointProduct::getProductType, pointProduct.getProductType());
        }
        if (pointProduct.getPointsRequired() != null) {
            chain.eq(PointProduct::getPointsRequired, pointProduct.getPointsRequired());
        }
        if (pointProduct.getStockQuantity() != null) {
            chain.eq(PointProduct::getStockQuantity, pointProduct.getStockQuantity());
        }
        if (pointProduct.getSoldQuantity() != null) {
            chain.eq(PointProduct::getSoldQuantity, pointProduct.getSoldQuantity());
        }
        if (pointProduct.getImageUrl() != null && !pointProduct.getImageUrl().isEmpty()) {
            chain.eq(PointProduct::getImageUrl, pointProduct.getImageUrl());
        }
        if (pointProduct.getDetailContent() != null && !pointProduct.getDetailContent().isEmpty()) {
            chain.eq(PointProduct::getDetailContent, pointProduct.getDetailContent());
        }
        if (pointProduct.getValidDays() != null) {
            chain.eq(PointProduct::getValidDays, pointProduct.getValidDays());
        }
        if (pointProduct.getBizStatus() != null && !pointProduct.getBizStatus().isEmpty()) {
            chain.eq(PointProduct::getBizStatus, pointProduct.getBizStatus());
        }
        if (pointProduct.getOrderNum() != null) {
            chain.eq(PointProduct::getOrderNum, pointProduct.getOrderNum());
        }
        if (pointProduct.getText1() != null && !pointProduct.getText1().isEmpty()) {
            chain.eq(PointProduct::getText1, pointProduct.getText1());
        }
        if (pointProduct.getText2() != null && !pointProduct.getText2().isEmpty()) {
            chain.eq(PointProduct::getText2, pointProduct.getText2());
        }
        if (pointProduct.getText3() != null && !pointProduct.getText3().isEmpty()) {
            chain.eq(PointProduct::getText3, pointProduct.getText3());
        }
        if (pointProduct.getJsonData() != null && !pointProduct.getJsonData().isEmpty()) {
            chain.eq(PointProduct::getJsonData, pointProduct.getJsonData());
        }
        if (pointProduct.getStatus() != null && !pointProduct.getStatus().isEmpty()) {
            chain.eq(PointProduct::getStatus, pointProduct.getStatus());
        }
        return chain;
    }

    @Override
    public Page<PointProduct> page(PointProduct pointProduct, int pageNum, int pageSize) {
        return selectList(pointProduct).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(PointProduct pointProduct, HttpServletResponse response) {
        List<PointProduct> list = selectList(pointProduct).list();
        ExcelUtil<PointProduct> util = new ExcelUtil<>(PointProduct.class);
        util.exportExcel(response, list, "积分商城商品数据");
    }


}
