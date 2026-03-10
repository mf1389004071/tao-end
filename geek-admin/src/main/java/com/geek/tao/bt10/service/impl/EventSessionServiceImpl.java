package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventSession;
import com.geek.tao.bt10.mapper.EventSessionMapper;
import com.geek.tao.bt10.service.IEventSessionService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 周期活动的单场次 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventSessionServiceImpl extends ServiceImpl<EventSessionMapper, EventSession> implements IEventSessionService {

    private QueryChain<EventSession> selectList(EventSession eventSession) {
        QueryChain<EventSession> chain = this.queryChain();
        if (eventSession.getEventId() != null) {
            chain.eq(EventSession::getEventId, eventSession.getEventId());
        }
        if (eventSession.getSessionDate() != null) {
            chain.eq(EventSession::getSessionDate, eventSession.getSessionDate());
        }
        if (eventSession.getStartTime() != null) {
            chain.eq(EventSession::getStartTime, eventSession.getStartTime());
        }
        if (eventSession.getEndTime() != null) {
            chain.eq(EventSession::getEndTime, eventSession.getEndTime());
        }
        if (eventSession.getBizStatus() != null && !eventSession.getBizStatus().isEmpty()) {
            chain.eq(EventSession::getBizStatus, eventSession.getBizStatus());
        }
        if (eventSession.getCheckInCount() != null) {
            chain.eq(EventSession::getCheckInCount, eventSession.getCheckInCount());
        }
        if (eventSession.getSummaryText() != null && !eventSession.getSummaryText().isEmpty()) {
            chain.eq(EventSession::getSummaryText, eventSession.getSummaryText());
        }
        if (eventSession.getMeetingUrl() != null && !eventSession.getMeetingUrl().isEmpty()) {
            chain.eq(EventSession::getMeetingUrl, eventSession.getMeetingUrl());
        }
        if (eventSession.getStatus() != null && !eventSession.getStatus().isEmpty()) {
            chain.eq(EventSession::getStatus, eventSession.getStatus());
        }
        return chain;
    }

    @Override
    public Page<EventSession> page(EventSession eventSession, int pageNum, int pageSize) {
        return selectList(eventSession).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(EventSession eventSession, HttpServletResponse response) {
        List<EventSession> list = selectList(eventSession).list();
        ExcelUtil<EventSession> util = new ExcelUtil<>(EventSession.class);
        util.exportExcel(response, list, "周期活动的单场次数据");
    }


}
