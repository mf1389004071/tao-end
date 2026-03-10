package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserInvite;
import com.geek.tao.bt10.mapper.UserInviteMapper;
import com.geek.tao.bt10.service.IUserInviteService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 邀请关系与奖励记录 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserInviteServiceImpl extends ServiceImpl<UserInviteMapper, UserInvite> implements IUserInviteService {

    private QueryChain<UserInvite> selectList(UserInvite userInvite) {
        QueryChain<UserInvite> chain = this.queryChain();
        if (userInvite.getUserId() != null) {
            chain.eq(UserInvite::getUserId, userInvite.getUserId());
        }
        if (userInvite.getInviterId() != null) {
            chain.eq(UserInvite::getInviterId, userInvite.getInviterId());
        }
        if (userInvite.getInviteCode() != null && !userInvite.getInviteCode().isEmpty()) {
            chain.eq(UserInvite::getInviteCode, userInvite.getInviteCode());
        }
        if (userInvite.getInviteTime() != null) {
            chain.eq(UserInvite::getInviteTime, userInvite.getInviteTime());
        }
        if (userInvite.getRewardStatus() != null && !userInvite.getRewardStatus().isEmpty()) {
            chain.eq(UserInvite::getRewardStatus, userInvite.getRewardStatus());
        }
        if (userInvite.getRewardClaimed() != null) {
            chain.eq(UserInvite::getRewardClaimed, userInvite.getRewardClaimed());
        }
        if (userInvite.getRewardPoints() != null) {
            chain.eq(UserInvite::getRewardPoints, userInvite.getRewardPoints());
        }
        if (userInvite.getRewardContrib() != null) {
            chain.eq(UserInvite::getRewardContrib, userInvite.getRewardContrib());
        }
        if (userInvite.getStatus() != null && !userInvite.getStatus().isEmpty()) {
            chain.eq(UserInvite::getStatus, userInvite.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserInvite> page(UserInvite userInvite, int pageNum, int pageSize) {
        return selectList(userInvite).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserInvite userInvite, HttpServletResponse response) {
        List<UserInvite> list = selectList(userInvite).list();
        ExcelUtil<UserInvite> util = new ExcelUtil<>(UserInvite.class);
        util.exportExcel(response, list, "邀请关系与奖励记录数据");
    }


}
