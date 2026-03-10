package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.PointRedemption;
import com.geek.tao.bt10.mapper.PointRedemptionMapper;
import com.geek.tao.bt10.service.IPointRedemptionService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户积分兑换记录 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class PointRedemptionServiceImpl extends ServiceImpl<PointRedemptionMapper, PointRedemption> implements IPointRedemptionService {

    private QueryChain<PointRedemption> selectList(PointRedemption pointRedemption) {
        QueryChain<PointRedemption> chain = this.queryChain();
        if (pointRedemption.getUserId() != null) {
            chain.eq(PointRedemption::getUserId, pointRedemption.getUserId());
        }
        if (pointRedemption.getProductId() != null) {
            chain.eq(PointRedemption::getProductId, pointRedemption.getProductId());
        }
        if (pointRedemption.getPointsUsed() != null) {
            chain.eq(PointRedemption::getPointsUsed, pointRedemption.getPointsUsed());
        }
        if (pointRedemption.getBizStatus() != null && !pointRedemption.getBizStatus().isEmpty()) {
            chain.eq(PointRedemption::getBizStatus, pointRedemption.getBizStatus());
        }
        if (pointRedemption.getRedemptionCode() != null && !pointRedemption.getRedemptionCode().isEmpty()) {
            chain.eq(PointRedemption::getRedemptionCode, pointRedemption.getRedemptionCode());
        }
        if (pointRedemption.getUsedTime() != null) {
            chain.eq(PointRedemption::getUsedTime, pointRedemption.getUsedTime());
        }
        if (pointRedemption.getExpiredTime() != null) {
            chain.eq(PointRedemption::getExpiredTime, pointRedemption.getExpiredTime());
        }
        if (pointRedemption.getText1() != null && !pointRedemption.getText1().isEmpty()) {
            chain.eq(PointRedemption::getText1, pointRedemption.getText1());
        }
        if (pointRedemption.getText2() != null && !pointRedemption.getText2().isEmpty()) {
            chain.eq(PointRedemption::getText2, pointRedemption.getText2());
        }
        if (pointRedemption.getText3() != null && !pointRedemption.getText3().isEmpty()) {
            chain.eq(PointRedemption::getText3, pointRedemption.getText3());
        }
        if (pointRedemption.getJsonData() != null && !pointRedemption.getJsonData().isEmpty()) {
            chain.eq(PointRedemption::getJsonData, pointRedemption.getJsonData());
        }
        if (pointRedemption.getStatus() != null && !pointRedemption.getStatus().isEmpty()) {
            chain.eq(PointRedemption::getStatus, pointRedemption.getStatus());
        }
        return chain;
    }

    @Override
    public Page<PointRedemption> page(PointRedemption pointRedemption, int pageNum, int pageSize) {
        return selectList(pointRedemption).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(PointRedemption pointRedemption, HttpServletResponse response) {
        List<PointRedemption> list = selectList(pointRedemption).list();
        ExcelUtil<PointRedemption> util = new ExcelUtil<>(PointRedemption.class);
        util.exportExcel(response, list, "用户积分兑换记录数据");
    }


}
