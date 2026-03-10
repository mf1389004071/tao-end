package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserPointLogs;
import com.geek.tao.bt10.mapper.UserPointLogsMapper;
import com.geek.tao.bt10.service.IUserPointLogsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户积分收支流水 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserPointLogsServiceImpl extends ServiceImpl<UserPointLogsMapper, UserPointLogs> implements IUserPointLogsService {

    private QueryChain<UserPointLogs> selectList(UserPointLogs userPointLogs) {
        QueryChain<UserPointLogs> chain = this.queryChain();
        if (userPointLogs.getUserId() != null) {
            chain.eq(UserPointLogs::getUserId, userPointLogs.getUserId());
        }
        if (userPointLogs.getActionType() != null && !userPointLogs.getActionType().isEmpty()) {
            chain.eq(UserPointLogs::getActionType, userPointLogs.getActionType());
        }
        if (userPointLogs.getPoints() != null) {
            chain.eq(UserPointLogs::getPoints, userPointLogs.getPoints());
        }
        if (userPointLogs.getBalanceBefore() != null) {
            chain.eq(UserPointLogs::getBalanceBefore, userPointLogs.getBalanceBefore());
        }
        if (userPointLogs.getBalanceAfter() != null) {
            chain.eq(UserPointLogs::getBalanceAfter, userPointLogs.getBalanceAfter());
        }
        if (userPointLogs.getRelatedType() != null && !userPointLogs.getRelatedType().isEmpty()) {
            chain.eq(UserPointLogs::getRelatedType, userPointLogs.getRelatedType());
        }
        if (userPointLogs.getRelatedId() != null) {
            chain.eq(UserPointLogs::getRelatedId, userPointLogs.getRelatedId());
        }
        if (userPointLogs.getExpiredTime() != null) {
            chain.eq(UserPointLogs::getExpiredTime, userPointLogs.getExpiredTime());
        }
        if (userPointLogs.getStatus() != null && !userPointLogs.getStatus().isEmpty()) {
            chain.eq(UserPointLogs::getStatus, userPointLogs.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserPointLogs> page(UserPointLogs userPointLogs, int pageNum, int pageSize) {
        return selectList(userPointLogs).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserPointLogs userPointLogs, HttpServletResponse response) {
        List<UserPointLogs> list = selectList(userPointLogs).list();
        ExcelUtil<UserPointLogs> util = new ExcelUtil<>(UserPointLogs.class);
        util.exportExcel(response, list, "用户积分收支流水数据");
    }


}
