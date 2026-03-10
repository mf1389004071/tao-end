package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventInfoTags;
import com.geek.tao.bt10.mapper.EventInfoTagsMapper;
import com.geek.tao.bt10.service.IEventInfoTagsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动与标签多对多关联 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventInfoTagsServiceImpl extends ServiceImpl<EventInfoTagsMapper, EventInfoTags> implements IEventInfoTagsService {

    private QueryChain<EventInfoTags> selectList(EventInfoTags eventInfoTags) {
        QueryChain<EventInfoTags> chain = this.queryChain();
        if (eventInfoTags.getEventId() != null) {
            chain.eq(EventInfoTags::getEventId, eventInfoTags.getEventId());
        }
        if (eventInfoTags.getTagId() != null) {
            chain.eq(EventInfoTags::getTagId, eventInfoTags.getTagId());
        }
        if (eventInfoTags.getOrderNum() != null) {
            chain.eq(EventInfoTags::getOrderNum, eventInfoTags.getOrderNum());
        }
        return chain;
    }

    @Override
    public Page<EventInfoTags> page(EventInfoTags eventInfoTags, int pageNum, int pageSize) {
        return selectList(eventInfoTags).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(EventInfoTags eventInfoTags, HttpServletResponse response) {
        List<EventInfoTags> list = selectList(eventInfoTags).list();
        ExcelUtil<EventInfoTags> util = new ExcelUtil<>(EventInfoTags.class);
        util.exportExcel(response, list, "活动与标签多对多关联数据");
    }


}
