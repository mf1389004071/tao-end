package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.CommunityInfo;
import com.geek.tao.bt10.mapper.CommunityInfoMapper;
import com.geek.tao.bt10.service.ICommunityInfoService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 合伙人创建的社群 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class CommunityInfoServiceImpl extends ServiceImpl<CommunityInfoMapper, CommunityInfo> implements ICommunityInfoService {

    private QueryChain<CommunityInfo> selectList(CommunityInfo communityInfo) {
        QueryChain<CommunityInfo> chain = this.queryChain();
        if (communityInfo.getName() != null && !communityInfo.getName().isEmpty()) {
            chain.like(CommunityInfo::getName, communityInfo.getName());
        }
        if (communityInfo.getOwnerId() != null) {
            chain.eq(CommunityInfo::getOwnerId, communityInfo.getOwnerId());
        }
        if (communityInfo.getIsPublic() != null) {
            chain.eq(CommunityInfo::getIsPublic, communityInfo.getIsPublic());
        }
        if (communityInfo.getMaxMembers() != null) {
            chain.eq(CommunityInfo::getMaxMembers, communityInfo.getMaxMembers());
        }
        if (communityInfo.getMemberCount() != null) {
            chain.eq(CommunityInfo::getMemberCount, communityInfo.getMemberCount());
        }
        if (communityInfo.getCoverImageUrl() != null && !communityInfo.getCoverImageUrl().isEmpty()) {
            chain.eq(CommunityInfo::getCoverImageUrl, communityInfo.getCoverImageUrl());
        }
        if (communityInfo.getCity() != null && !communityInfo.getCity().isEmpty()) {
            chain.eq(CommunityInfo::getCity, communityInfo.getCity());
        }
        if (communityInfo.getBizStatus() != null && !communityInfo.getBizStatus().isEmpty()) {
            chain.eq(CommunityInfo::getBizStatus, communityInfo.getBizStatus());
        }
        if (communityInfo.getText1() != null && !communityInfo.getText1().isEmpty()) {
            chain.eq(CommunityInfo::getText1, communityInfo.getText1());
        }
        if (communityInfo.getText2() != null && !communityInfo.getText2().isEmpty()) {
            chain.eq(CommunityInfo::getText2, communityInfo.getText2());
        }
        if (communityInfo.getText3() != null && !communityInfo.getText3().isEmpty()) {
            chain.eq(CommunityInfo::getText3, communityInfo.getText3());
        }
        if (communityInfo.getJsonData() != null && !communityInfo.getJsonData().isEmpty()) {
            chain.eq(CommunityInfo::getJsonData, communityInfo.getJsonData());
        }
        if (communityInfo.getStatus() != null && !communityInfo.getStatus().isEmpty()) {
            chain.eq(CommunityInfo::getStatus, communityInfo.getStatus());
        }
        return chain;
    }

    @Override
    public Page<CommunityInfo> page(CommunityInfo communityInfo, int pageNum, int pageSize) {
        return selectList(communityInfo).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(CommunityInfo communityInfo, HttpServletResponse response) {
        List<CommunityInfo> list = selectList(communityInfo).list();
        ExcelUtil<CommunityInfo> util = new ExcelUtil<>(CommunityInfo.class);
        util.exportExcel(response, list, "合伙人创建的社群数据");
    }


}
