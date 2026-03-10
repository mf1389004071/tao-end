package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserContribLogs;
import com.geek.tao.bt10.mapper.UserContribLogsMapper;
import com.geek.tao.bt10.service.IUserContribLogsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户贡献点收支流水 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserContribLogsServiceImpl extends ServiceImpl<UserContribLogsMapper, UserContribLogs> implements IUserContribLogsService {

    private QueryChain<UserContribLogs> selectList(UserContribLogs userContribLogs) {
        QueryChain<UserContribLogs> chain = this.queryChain();
        if (userContribLogs.getUserId() != null) {
            chain.eq(UserContribLogs::getUserId, userContribLogs.getUserId());
        }
        if (userContribLogs.getActionType() != null && !userContribLogs.getActionType().isEmpty()) {
            chain.eq(UserContribLogs::getActionType, userContribLogs.getActionType());
        }
        if (userContribLogs.getAmount() != null) {
            chain.eq(UserContribLogs::getAmount, userContribLogs.getAmount());
        }
        if (userContribLogs.getBalanceBefore() != null) {
            chain.eq(UserContribLogs::getBalanceBefore, userContribLogs.getBalanceBefore());
        }
        if (userContribLogs.getBalanceAfter() != null) {
            chain.eq(UserContribLogs::getBalanceAfter, userContribLogs.getBalanceAfter());
        }
        if (userContribLogs.getRelatedType() != null && !userContribLogs.getRelatedType().isEmpty()) {
            chain.eq(UserContribLogs::getRelatedType, userContribLogs.getRelatedType());
        }
        if (userContribLogs.getRelatedId() != null) {
            chain.eq(UserContribLogs::getRelatedId, userContribLogs.getRelatedId());
        }
        if (userContribLogs.getPaymentNo() != null && !userContribLogs.getPaymentNo().isEmpty()) {
            chain.eq(UserContribLogs::getPaymentNo, userContribLogs.getPaymentNo());
        }
        if (userContribLogs.getStatus() != null && !userContribLogs.getStatus().isEmpty()) {
            chain.eq(UserContribLogs::getStatus, userContribLogs.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserContribLogs> page(UserContribLogs userContribLogs, int pageNum, int pageSize) {
        return selectList(userContribLogs).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserContribLogs userContribLogs, HttpServletResponse response) {
        List<UserContribLogs> list = selectList(userContribLogs).list();
        ExcelUtil<UserContribLogs> util = new ExcelUtil<>(UserContribLogs.class);
        util.exportExcel(response, list, "用户贡献点收支流水数据");
    }


}
