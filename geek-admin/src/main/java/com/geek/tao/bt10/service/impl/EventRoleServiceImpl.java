package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.EventRole;
import com.geek.tao.bt10.mapper.EventRoleMapper;
import com.geek.tao.bt10.service.IEventRoleService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动所需角色 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class EventRoleServiceImpl extends ServiceImpl<EventRoleMapper, EventRole> implements IEventRoleService {

    private QueryChain<EventRole> selectList(EventRole eventRole) {
        QueryChain<EventRole> chain = this.queryChain();
        if (eventRole.getEventId() != null) {
            chain.eq(EventRole::getEventId, eventRole.getEventId());
        }
        if (eventRole.getRoleName() != null && !eventRole.getRoleName().isEmpty()) {
            chain.like(EventRole::getRoleName, eventRole.getRoleName());
        }
        if (eventRole.getRoleDescription() != null && !eventRole.getRoleDescription().isEmpty()) {
            chain.eq(EventRole::getRoleDescription, eventRole.getRoleDescription());
        }
        if (eventRole.getMaxParticipants() != null) {
            chain.eq(EventRole::getMaxParticipants, eventRole.getMaxParticipants());
        }
        if (eventRole.getCurrentParticipants() != null) {
            chain.eq(EventRole::getCurrentParticipants, eventRole.getCurrentParticipants());
        }
        if (eventRole.getPermissions() != null && !eventRole.getPermissions().isEmpty()) {
            chain.eq(EventRole::getPermissions, eventRole.getPermissions());
        }
        if (eventRole.getPointsReward() != null) {
            chain.eq(EventRole::getPointsReward, eventRole.getPointsReward());
        }
        if (eventRole.getContribReward() != null) {
            chain.eq(EventRole::getContribReward, eventRole.getContribReward());
        }
        if (eventRole.getResponsibilities() != null && !eventRole.getResponsibilities().isEmpty()) {
            chain.eq(EventRole::getResponsibilities, eventRole.getResponsibilities());
        }
        if (eventRole.getRequirements() != null && !eventRole.getRequirements().isEmpty()) {
            chain.eq(EventRole::getRequirements, eventRole.getRequirements());
        }
        if (eventRole.getOrderNum() != null) {
            chain.eq(EventRole::getOrderNum, eventRole.getOrderNum());
        }
        if (eventRole.getStatus() != null && !eventRole.getStatus().isEmpty()) {
            chain.eq(EventRole::getStatus, eventRole.getStatus());
        }
        return chain;
    }

    @Override
    public Page<EventRole> page(EventRole eventRole, int pageNum, int pageSize) {
        return selectList(eventRole).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(EventRole eventRole, HttpServletResponse response) {
        List<EventRole> list = selectList(eventRole).list();
        ExcelUtil<EventRole> util = new ExcelUtil<>(EventRole.class);
        util.exportExcel(response, list, "活动所需角色数据");
    }


}
