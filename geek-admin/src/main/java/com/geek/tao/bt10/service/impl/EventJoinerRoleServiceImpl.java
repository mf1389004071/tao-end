package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventJoinerRole;
import com.geek.tao.bt10.mapper.EventJoinerRoleMapper;
import com.geek.tao.bt10.service.IEventJoinerRoleService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 报名记录与活动角色的分配关系 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventJoinerRoleServiceImpl extends ServiceImpl<EventJoinerRoleMapper, EventJoinerRole> implements IEventJoinerRoleService {

    private QueryChain<EventJoinerRole> selectList(EventJoinerRole eventJoinerRole) {
        QueryChain<EventJoinerRole> chain = this.queryChain();
        if (eventJoinerRole.getJoinId() != null) {
            chain.eq(EventJoinerRole::getJoinId, eventJoinerRole.getJoinId());
        }
        if (eventJoinerRole.getRoleId() != null) {
            chain.eq(EventJoinerRole::getRoleId, eventJoinerRole.getRoleId());
        }
        if (eventJoinerRole.getAssignedTime() != null) {
            chain.eq(EventJoinerRole::getAssignedTime, eventJoinerRole.getAssignedTime());
        }
        if (eventJoinerRole.getConfirmedTime() != null) {
            chain.eq(EventJoinerRole::getConfirmedTime, eventJoinerRole.getConfirmedTime());
        }
        if (eventJoinerRole.getBizStatus() != null && !eventJoinerRole.getBizStatus().isEmpty()) {
            chain.eq(EventJoinerRole::getBizStatus, eventJoinerRole.getBizStatus());
        }
        return chain;
    }

    @Override
    public Page<EventJoinerRole> page(EventJoinerRole eventJoinerRole, int pageNum, int pageSize) {
        return selectList(eventJoinerRole).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(EventJoinerRole eventJoinerRole, HttpServletResponse response) {
        List<EventJoinerRole> list = selectList(eventJoinerRole).list();
        ExcelUtil<EventJoinerRole> util = new ExcelUtil<>(EventJoinerRole.class);
        util.exportExcel(response, list, "报名记录与活动角色的分配关系数据");
    }


}
