package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventCheckIn;
import com.geek.tao.bt10.mapper.EventCheckInMapper;
import com.geek.tao.bt10.service.IEventCheckInService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 单次签到记录 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventCheckInServiceImpl extends ServiceImpl<EventCheckInMapper, EventCheckIn> implements IEventCheckInService {

    private QueryChain<EventCheckIn> selectList(EventCheckIn eventCheckIn) {
        QueryChain<EventCheckIn> chain = this.queryChain();
        if (eventCheckIn.getJoinId() != null) {
            chain.eq(EventCheckIn::getJoinId, eventCheckIn.getJoinId());
        }
        if (eventCheckIn.getSessionId() != null) {
            chain.eq(EventCheckIn::getSessionId, eventCheckIn.getSessionId());
        }
        if (eventCheckIn.getCheckInTime() != null) {
            chain.eq(EventCheckIn::getCheckInTime, eventCheckIn.getCheckInTime());
        }
        if (eventCheckIn.getCheckInMethod() != null && !eventCheckIn.getCheckInMethod().isEmpty()) {
            chain.eq(EventCheckIn::getCheckInMethod, eventCheckIn.getCheckInMethod());
        }
        if (eventCheckIn.getCheckInLocation() != null && !eventCheckIn.getCheckInLocation().isEmpty()) {
            chain.eq(EventCheckIn::getCheckInLocation, eventCheckIn.getCheckInLocation());
        }
        if (eventCheckIn.getOperatorId() != null) {
            chain.eq(EventCheckIn::getOperatorId, eventCheckIn.getOperatorId());
        }
        if (eventCheckIn.getStatus() != null && !eventCheckIn.getStatus().isEmpty()) {
            chain.eq(EventCheckIn::getStatus, eventCheckIn.getStatus());
        }
        return chain;
    }

    @Override
    public Page<EventCheckIn> page(EventCheckIn eventCheckIn, int pageNum, int pageSize) {
        return selectList(eventCheckIn).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(EventCheckIn eventCheckIn, HttpServletResponse response) {
        List<EventCheckIn> list = selectList(eventCheckIn).list();
        ExcelUtil<EventCheckIn> util = new ExcelUtil<>(EventCheckIn.class);
        util.exportExcel(response, list, "单次签到记录数据");
    }


}
