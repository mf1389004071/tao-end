package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventJoin;
import com.geek.tao.bt10.mapper.EventJoinMapper;
import com.geek.tao.bt10.service.IEventJoinService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户活动报名记录 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventJoinServiceImpl extends ServiceImpl<EventJoinMapper, EventJoin> implements IEventJoinService {

    private QueryChain<EventJoin> selectList(EventJoin eventJoin) {
        QueryChain<EventJoin> chain = this.queryChain();
        if (eventJoin.getEventId() != null) {
            chain.eq(EventJoin::getEventId, eventJoin.getEventId());
        }
        if (eventJoin.getUserId() != null) {
            chain.eq(EventJoin::getUserId, eventJoin.getUserId());
        }
        if (eventJoin.getBizStatus() != null && !eventJoin.getBizStatus().isEmpty()) {
            chain.eq(EventJoin::getBizStatus, eventJoin.getBizStatus());
        }
        if (eventJoin.getJoinForm() != null && !eventJoin.getJoinForm().isEmpty()) {
            chain.eq(EventJoin::getJoinForm, eventJoin.getJoinForm());
        }
        if (eventJoin.getPaymentStatus() != null && !eventJoin.getPaymentStatus().isEmpty()) {
            chain.eq(EventJoin::getPaymentStatus, eventJoin.getPaymentStatus());
        }
        if (eventJoin.getPaymentAmount() != null) {
            chain.eq(EventJoin::getPaymentAmount, eventJoin.getPaymentAmount());
        }
        if (eventJoin.getPaymentMethod() != null && !eventJoin.getPaymentMethod().isEmpty()) {
            chain.eq(EventJoin::getPaymentMethod, eventJoin.getPaymentMethod());
        }
        if (eventJoin.getPaymentNo() != null && !eventJoin.getPaymentNo().isEmpty()) {
            chain.eq(EventJoin::getPaymentNo, eventJoin.getPaymentNo());
        }
        if (eventJoin.getCheckedInTime() != null) {
            chain.eq(EventJoin::getCheckedInTime, eventJoin.getCheckedInTime());
        }
        if (eventJoin.getCheckInMethod() != null && !eventJoin.getCheckInMethod().isEmpty()) {
            chain.eq(EventJoin::getCheckInMethod, eventJoin.getCheckInMethod());
        }
        if (eventJoin.getCheckInLocation() != null && !eventJoin.getCheckInLocation().isEmpty()) {
            chain.eq(EventJoin::getCheckInLocation, eventJoin.getCheckInLocation());
        }
        if (eventJoin.getConversionStatus() != null && !eventJoin.getConversionStatus().isEmpty()) {
            chain.eq(EventJoin::getConversionStatus, eventJoin.getConversionStatus());
        }
        if (eventJoin.getConversionProductId() != null) {
            chain.eq(EventJoin::getConversionProductId, eventJoin.getConversionProductId());
        }
        if (eventJoin.getConversionAmount() != null) {
            chain.eq(EventJoin::getConversionAmount, eventJoin.getConversionAmount());
        }
        if (eventJoin.getConversionNotes() != null && !eventJoin.getConversionNotes().isEmpty()) {
            chain.eq(EventJoin::getConversionNotes, eventJoin.getConversionNotes());
        }
        if (eventJoin.getText1() != null && !eventJoin.getText1().isEmpty()) {
            chain.eq(EventJoin::getText1, eventJoin.getText1());
        }
        if (eventJoin.getText2() != null && !eventJoin.getText2().isEmpty()) {
            chain.eq(EventJoin::getText2, eventJoin.getText2());
        }
        if (eventJoin.getText3() != null && !eventJoin.getText3().isEmpty()) {
            chain.eq(EventJoin::getText3, eventJoin.getText3());
        }
        if (eventJoin.getJsonData() != null && !eventJoin.getJsonData().isEmpty()) {
            chain.eq(EventJoin::getJsonData, eventJoin.getJsonData());
        }
        if (eventJoin.getStatus() != null && !eventJoin.getStatus().isEmpty()) {
            chain.eq(EventJoin::getStatus, eventJoin.getStatus());
        }
        return chain;
    }

    @Override
    public Page<EventJoin> page(EventJoin eventJoin, int pageNum, int pageSize) {
        return selectList(eventJoin).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(EventJoin eventJoin, HttpServletResponse response) {
        List<EventJoin> list = selectList(eventJoin).list();
        ExcelUtil<EventJoin> util = new ExcelUtil<>(EventJoin.class);
        util.exportExcel(response, list, "用户活动报名记录数据");
    }


}
