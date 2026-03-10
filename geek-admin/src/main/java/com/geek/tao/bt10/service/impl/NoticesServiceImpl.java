package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.Notices;
import com.geek.tao.bt10.mapper.NoticesMapper;
import com.geek.tao.bt10.service.INoticesService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统级通知与公告 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class NoticesServiceImpl extends ServiceImpl<NoticesMapper, Notices> implements INoticesService {

    private QueryChain<Notices> selectList(Notices notices) {
        QueryChain<Notices> chain = this.queryChain();
        if (notices.getTitle() != null && !notices.getTitle().isEmpty()) {
            chain.eq(Notices::getTitle, notices.getTitle());
        }
        if (notices.getContent() != null && !notices.getContent().isEmpty()) {
            chain.eq(Notices::getContent, notices.getContent());
        }
        if (notices.getType() != null && !notices.getType().isEmpty()) {
            chain.eq(Notices::getType, notices.getType());
        }
        if (notices.getIsUrgent() != null) {
            chain.eq(Notices::getIsUrgent, notices.getIsUrgent());
        }
        if (notices.getPublishTime() != null) {
            chain.eq(Notices::getPublishTime, notices.getPublishTime());
        }
        if (notices.getText1() != null && !notices.getText1().isEmpty()) {
            chain.eq(Notices::getText1, notices.getText1());
        }
        if (notices.getText2() != null && !notices.getText2().isEmpty()) {
            chain.eq(Notices::getText2, notices.getText2());
        }
        if (notices.getText3() != null && !notices.getText3().isEmpty()) {
            chain.eq(Notices::getText3, notices.getText3());
        }
        if (notices.getJsonData() != null && !notices.getJsonData().isEmpty()) {
            chain.eq(Notices::getJsonData, notices.getJsonData());
        }
        if (notices.getStatus() != null && !notices.getStatus().isEmpty()) {
            chain.eq(Notices::getStatus, notices.getStatus());
        }
        return chain;
    }

    @Override
    public Page<Notices> page(Notices notices, int pageNum, int pageSize) {
        return selectList(notices).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(Notices notices, HttpServletResponse response) {
        List<Notices> list = selectList(notices).list();
        ExcelUtil<Notices> util = new ExcelUtil<>(Notices.class);
        util.exportExcel(response, list, "系统级通知与公告数据");
    }


}
