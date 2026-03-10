package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserGrowth;
import com.geek.tao.bt10.mapper.UserGrowthMapper;
import com.geek.tao.bt10.service.IUserGrowthService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户成长阶段变更历史 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserGrowthServiceImpl extends ServiceImpl<UserGrowthMapper, UserGrowth> implements IUserGrowthService {

    private QueryChain<UserGrowth> selectList(UserGrowth userGrowth) {
        QueryChain<UserGrowth> chain = this.queryChain();
        if (userGrowth.getUserId() != null) {
            chain.eq(UserGrowth::getUserId, userGrowth.getUserId());
        }
        if (userGrowth.getStageFrom() != null && !userGrowth.getStageFrom().isEmpty()) {
            chain.eq(UserGrowth::getStageFrom, userGrowth.getStageFrom());
        }
        if (userGrowth.getStageTo() != null && !userGrowth.getStageTo().isEmpty()) {
            chain.eq(UserGrowth::getStageTo, userGrowth.getStageTo());
        }
        if (userGrowth.getTriggerType() != null && !userGrowth.getTriggerType().isEmpty()) {
            chain.eq(UserGrowth::getTriggerType, userGrowth.getTriggerType());
        }
        if (userGrowth.getTriggerData() != null && !userGrowth.getTriggerData().isEmpty()) {
            chain.eq(UserGrowth::getTriggerData, userGrowth.getTriggerData());
        }
        if (userGrowth.getStatus() != null && !userGrowth.getStatus().isEmpty()) {
            chain.eq(UserGrowth::getStatus, userGrowth.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserGrowth> page(UserGrowth userGrowth, int pageNum, int pageSize) {
        return selectList(userGrowth).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserGrowth userGrowth, HttpServletResponse response) {
        List<UserGrowth> list = selectList(userGrowth).list();
        ExcelUtil<UserGrowth> util = new ExcelUtil<>(UserGrowth.class);
        util.exportExcel(response, list, "用户成长阶段变更历史数据");
    }


}
