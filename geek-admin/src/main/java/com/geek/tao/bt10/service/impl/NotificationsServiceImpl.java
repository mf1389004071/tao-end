package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.Notifications;
import com.geek.tao.bt10.mapper.NotificationsMapper;
import com.geek.tao.bt10.service.INotificationsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户站内通知 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class NotificationsServiceImpl extends ServiceImpl<NotificationsMapper, Notifications> implements INotificationsService {

    private QueryChain<Notifications> selectList(Notifications notifications) {
        QueryChain<Notifications> chain = this.queryChain();
        if (notifications.getUserId() != null) {
            chain.eq(Notifications::getUserId, notifications.getUserId());
        }
        if (notifications.getTitle() != null && !notifications.getTitle().isEmpty()) {
            chain.eq(Notifications::getTitle, notifications.getTitle());
        }
        if (notifications.getContent() != null && !notifications.getContent().isEmpty()) {
            chain.eq(Notifications::getContent, notifications.getContent());
        }
        if (notifications.getNotificationType() != null && !notifications.getNotificationType().isEmpty()) {
            chain.eq(Notifications::getNotificationType, notifications.getNotificationType());
        }
        if (notifications.getRelatedType() != null && !notifications.getRelatedType().isEmpty()) {
            chain.eq(Notifications::getRelatedType, notifications.getRelatedType());
        }
        if (notifications.getRelatedId() != null) {
            chain.eq(Notifications::getRelatedId, notifications.getRelatedId());
        }
        if (notifications.getIsRead() != null) {
            chain.eq(Notifications::getIsRead, notifications.getIsRead());
        }
        if (notifications.getReadTime() != null) {
            chain.eq(Notifications::getReadTime, notifications.getReadTime());
        }
        if (notifications.getStatus() != null && !notifications.getStatus().isEmpty()) {
            chain.eq(Notifications::getStatus, notifications.getStatus());
        }
        return chain;
    }

    @Override
    public Page<Notifications> page(Notifications notifications, int pageNum, int pageSize) {
        return selectList(notifications).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(Notifications notifications, HttpServletResponse response) {
        List<Notifications> list = selectList(notifications).list();
        ExcelUtil<Notifications> util = new ExcelUtil<>(Notifications.class);
        util.exportExcel(response, list, "用户站内通知数据");
    }


}
