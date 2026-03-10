package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserActivityLogs;
import com.geek.tao.bt10.mapper.UserActivityLogsMapper;
import com.geek.tao.bt10.service.IUserActivityLogsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户行为轨迹日志 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserActivityLogsServiceImpl extends ServiceImpl<UserActivityLogsMapper, UserActivityLogs> implements IUserActivityLogsService {

    private QueryChain<UserActivityLogs> selectList(UserActivityLogs userActivityLogs) {
        QueryChain<UserActivityLogs> chain = this.queryChain();
        if (userActivityLogs.getUserId() != null) {
            chain.eq(UserActivityLogs::getUserId, userActivityLogs.getUserId());
        }
        if (userActivityLogs.getOccurredTime() != null) {
            chain.eq(UserActivityLogs::getOccurredTime, userActivityLogs.getOccurredTime());
        }
        if (userActivityLogs.getIpAddress() != null && !userActivityLogs.getIpAddress().isEmpty()) {
            chain.eq(UserActivityLogs::getIpAddress, userActivityLogs.getIpAddress());
        }
        if (userActivityLogs.getDevice() != null && !userActivityLogs.getDevice().isEmpty()) {
            chain.eq(UserActivityLogs::getDevice, userActivityLogs.getDevice());
        }
        if (userActivityLogs.getEventType() != null && !userActivityLogs.getEventType().isEmpty()) {
            chain.eq(UserActivityLogs::getEventType, userActivityLogs.getEventType());
        }
        if (userActivityLogs.getEventSourceType() != null && !userActivityLogs.getEventSourceType().isEmpty()) {
            chain.eq(UserActivityLogs::getEventSourceType, userActivityLogs.getEventSourceType());
        }
        if (userActivityLogs.getEventSourceId() != null) {
            chain.eq(UserActivityLogs::getEventSourceId, userActivityLogs.getEventSourceId());
        }
        if (userActivityLogs.getEventTags() != null && !userActivityLogs.getEventTags().isEmpty()) {
            chain.eq(UserActivityLogs::getEventTags, userActivityLogs.getEventTags());
        }
        if (userActivityLogs.getPointChange() != null) {
            chain.eq(UserActivityLogs::getPointChange, userActivityLogs.getPointChange());
        }
        if (userActivityLogs.getPointBalanceBefore() != null) {
            chain.eq(UserActivityLogs::getPointBalanceBefore, userActivityLogs.getPointBalanceBefore());
        }
        if (userActivityLogs.getPointBalanceAfter() != null) {
            chain.eq(UserActivityLogs::getPointBalanceAfter, userActivityLogs.getPointBalanceAfter());
        }
        if (userActivityLogs.getContribChange() != null) {
            chain.eq(UserActivityLogs::getContribChange, userActivityLogs.getContribChange());
        }
        if (userActivityLogs.getContribBalanceBefore() != null) {
            chain.eq(UserActivityLogs::getContribBalanceBefore, userActivityLogs.getContribBalanceBefore());
        }
        if (userActivityLogs.getContribBalanceAfter() != null) {
            chain.eq(UserActivityLogs::getContribBalanceAfter, userActivityLogs.getContribBalanceAfter());
        }
        if (userActivityLogs.getExtra() != null && !userActivityLogs.getExtra().isEmpty()) {
            chain.eq(UserActivityLogs::getExtra, userActivityLogs.getExtra());
        }
        if (userActivityLogs.getStatus() != null && !userActivityLogs.getStatus().isEmpty()) {
            chain.eq(UserActivityLogs::getStatus, userActivityLogs.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserActivityLogs> page(UserActivityLogs userActivityLogs, int pageNum, int pageSize) {
        return selectList(userActivityLogs).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserActivityLogs userActivityLogs, HttpServletResponse response) {
        List<UserActivityLogs> list = selectList(userActivityLogs).list();
        ExcelUtil<UserActivityLogs> util = new ExcelUtil<>(UserActivityLogs.class);
        util.exportExcel(response, list, "用户行为轨迹日志数据");
    }


}
