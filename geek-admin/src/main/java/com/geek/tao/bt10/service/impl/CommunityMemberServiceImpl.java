package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.CommunityMember;
import com.geek.tao.bt10.mapper.CommunityMemberMapper;
import com.geek.tao.bt10.service.ICommunityMemberService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 社群与用户的成员关系 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class CommunityMemberServiceImpl extends ServiceImpl<CommunityMemberMapper, CommunityMember> implements ICommunityMemberService {

    private QueryChain<CommunityMember> selectList(CommunityMember communityMember) {
        QueryChain<CommunityMember> chain = this.queryChain();
        if (communityMember.getCommunityId() != null) {
            chain.eq(CommunityMember::getCommunityId, communityMember.getCommunityId());
        }
        if (communityMember.getUserId() != null) {
            chain.eq(CommunityMember::getUserId, communityMember.getUserId());
        }
        if (communityMember.getRole() != null && !communityMember.getRole().isEmpty()) {
            chain.eq(CommunityMember::getRole, communityMember.getRole());
        }
        if (communityMember.getBizStatus() != null && !communityMember.getBizStatus().isEmpty()) {
            chain.eq(CommunityMember::getBizStatus, communityMember.getBizStatus());
        }
        if (communityMember.getJoinedTime() != null) {
            chain.eq(CommunityMember::getJoinedTime, communityMember.getJoinedTime());
        }
        if (communityMember.getLeftTime() != null) {
            chain.eq(CommunityMember::getLeftTime, communityMember.getLeftTime());
        }
        if (communityMember.getStatus() != null && !communityMember.getStatus().isEmpty()) {
            chain.eq(CommunityMember::getStatus, communityMember.getStatus());
        }
        return chain;
    }

    @Override
    public Page<CommunityMember> page(CommunityMember communityMember, int pageNum, int pageSize) {
        return selectList(communityMember).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(CommunityMember communityMember, HttpServletResponse response) {
        List<CommunityMember> list = selectList(communityMember).list();
        ExcelUtil<CommunityMember> util = new ExcelUtil<>(CommunityMember.class);
        util.exportExcel(response, list, "社群与用户的成员关系数据");
    }


}
