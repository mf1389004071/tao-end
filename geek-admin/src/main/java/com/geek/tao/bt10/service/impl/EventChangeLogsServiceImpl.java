package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventChangeLogs;
import com.geek.tao.bt10.mapper.EventChangeLogsMapper;
import com.geek.tao.bt10.service.IEventChangeLogsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动关键信息变更记录 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventChangeLogsServiceImpl extends ServiceImpl<EventChangeLogsMapper, EventChangeLogs> implements IEventChangeLogsService {

    private QueryChain<EventChangeLogs> selectList(EventChangeLogs eventChangeLogs) {
        QueryChain<EventChangeLogs> chain = this.queryChain();
        if (eventChangeLogs.getEventId() != null) {
            chain.eq(EventChangeLogs::getEventId, eventChangeLogs.getEventId());
        }
        if (eventChangeLogs.getChangeField() != null && !eventChangeLogs.getChangeField().isEmpty()) {
            chain.eq(EventChangeLogs::getChangeField, eventChangeLogs.getChangeField());
        }
        if (eventChangeLogs.getOldValue() != null && !eventChangeLogs.getOldValue().isEmpty()) {
            chain.eq(EventChangeLogs::getOldValue, eventChangeLogs.getOldValue());
        }
        if (eventChangeLogs.getNewValue() != null && !eventChangeLogs.getNewValue().isEmpty()) {
            chain.eq(EventChangeLogs::getNewValue, eventChangeLogs.getNewValue());
        }
        if (eventChangeLogs.getChangeReason() != null && !eventChangeLogs.getChangeReason().isEmpty()) {
            chain.eq(EventChangeLogs::getChangeReason, eventChangeLogs.getChangeReason());
        }
        if (eventChangeLogs.getOperatorId() != null) {
            chain.eq(EventChangeLogs::getOperatorId, eventChangeLogs.getOperatorId());
        }
        if (eventChangeLogs.getOperatorType() != null && !eventChangeLogs.getOperatorType().isEmpty()) {
            chain.eq(EventChangeLogs::getOperatorType, eventChangeLogs.getOperatorType());
        }
        if (eventChangeLogs.getChangedTime() != null) {
            chain.eq(EventChangeLogs::getChangedTime, eventChangeLogs.getChangedTime());
        }
        if (eventChangeLogs.getMetadata() != null && !eventChangeLogs.getMetadata().isEmpty()) {
            chain.eq(EventChangeLogs::getMetadata, eventChangeLogs.getMetadata());
        }
        if (eventChangeLogs.getStatus() != null && !eventChangeLogs.getStatus().isEmpty()) {
            chain.eq(EventChangeLogs::getStatus, eventChangeLogs.getStatus());
        }
        return chain;
    }

    @Override
    public Page<EventChangeLogs> page(EventChangeLogs eventChangeLogs, int pageNum, int pageSize) {
        return selectList(eventChangeLogs).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(EventChangeLogs eventChangeLogs, HttpServletResponse response) {
        List<EventChangeLogs> list = selectList(eventChangeLogs).list();
        ExcelUtil<EventChangeLogs> util = new ExcelUtil<>(EventChangeLogs.class);
        util.exportExcel(response, list, "活动关键信息变更记录数据");
    }


}
