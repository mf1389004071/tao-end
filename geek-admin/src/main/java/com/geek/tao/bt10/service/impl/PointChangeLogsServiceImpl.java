package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.PointChangeLogs;
import com.geek.tao.bt10.mapper.PointChangeLogsMapper;
import com.geek.tao.bt10.service.IPointChangeLogsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 积分变动审计 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class PointChangeLogsServiceImpl extends ServiceImpl<PointChangeLogsMapper, PointChangeLogs> implements IPointChangeLogsService {

    private QueryChain<PointChangeLogs> selectList(PointChangeLogs pointChangeLogs) {
        QueryChain<PointChangeLogs> chain = this.queryChain();
        if (pointChangeLogs.getUserId() != null) {
            chain.eq(PointChangeLogs::getUserId, pointChangeLogs.getUserId());
        }
        if (pointChangeLogs.getChangeType() != null && !pointChangeLogs.getChangeType().isEmpty()) {
            chain.eq(PointChangeLogs::getChangeType, pointChangeLogs.getChangeType());
        }
        if (pointChangeLogs.getPointsChange() != null) {
            chain.eq(PointChangeLogs::getPointsChange, pointChangeLogs.getPointsChange());
        }
        if (pointChangeLogs.getBalanceBefore() != null) {
            chain.eq(PointChangeLogs::getBalanceBefore, pointChangeLogs.getBalanceBefore());
        }
        if (pointChangeLogs.getBalanceAfter() != null) {
            chain.eq(PointChangeLogs::getBalanceAfter, pointChangeLogs.getBalanceAfter());
        }
        if (pointChangeLogs.getChangeReason() != null && !pointChangeLogs.getChangeReason().isEmpty()) {
            chain.eq(PointChangeLogs::getChangeReason, pointChangeLogs.getChangeReason());
        }
        if (pointChangeLogs.getRelatedType() != null && !pointChangeLogs.getRelatedType().isEmpty()) {
            chain.eq(PointChangeLogs::getRelatedType, pointChangeLogs.getRelatedType());
        }
        if (pointChangeLogs.getRelatedId() != null) {
            chain.eq(PointChangeLogs::getRelatedId, pointChangeLogs.getRelatedId());
        }
        if (pointChangeLogs.getOperatorId() != null) {
            chain.eq(PointChangeLogs::getOperatorId, pointChangeLogs.getOperatorId());
        }
        if (pointChangeLogs.getOperatorType() != null && !pointChangeLogs.getOperatorType().isEmpty()) {
            chain.eq(PointChangeLogs::getOperatorType, pointChangeLogs.getOperatorType());
        }
        if (pointChangeLogs.getChangedTime() != null) {
            chain.eq(PointChangeLogs::getChangedTime, pointChangeLogs.getChangedTime());
        }
        if (pointChangeLogs.getMetadata() != null && !pointChangeLogs.getMetadata().isEmpty()) {
            chain.eq(PointChangeLogs::getMetadata, pointChangeLogs.getMetadata());
        }
        if (pointChangeLogs.getStatus() != null && !pointChangeLogs.getStatus().isEmpty()) {
            chain.eq(PointChangeLogs::getStatus, pointChangeLogs.getStatus());
        }
        return chain;
    }

    @Override
    public Page<PointChangeLogs> page(PointChangeLogs pointChangeLogs, int pageNum, int pageSize) {
        return selectList(pointChangeLogs).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(PointChangeLogs pointChangeLogs, HttpServletResponse response) {
        List<PointChangeLogs> list = selectList(pointChangeLogs).list();
        ExcelUtil<PointChangeLogs> util = new ExcelUtil<>(PointChangeLogs.class);
        util.exportExcel(response, list, "积分变动审计数据");
    }


}
