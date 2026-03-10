package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserIdentities;
import com.geek.tao.bt10.mapper.UserIdentitiesMapper;
import com.geek.tao.bt10.service.IUserIdentitiesService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户身份关系表 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserIdentitiesServiceImpl extends ServiceImpl<UserIdentitiesMapper, UserIdentities> implements IUserIdentitiesService {

    private QueryChain<UserIdentities> selectList(UserIdentities userIdentities) {
        QueryChain<UserIdentities> chain = this.queryChain();
        if (userIdentities.getUserId() != null) {
            chain.eq(UserIdentities::getUserId, userIdentities.getUserId());
        }
        if (userIdentities.getIdentityCode() != null && !userIdentities.getIdentityCode().isEmpty()) {
            chain.eq(UserIdentities::getIdentityCode, userIdentities.getIdentityCode());
        }
        if (userIdentities.getIsPrimary() != null) {
            chain.eq(UserIdentities::getIsPrimary, userIdentities.getIsPrimary());
        }
        if (userIdentities.getBizStatus() != null && !userIdentities.getBizStatus().isEmpty()) {
            chain.eq(UserIdentities::getBizStatus, userIdentities.getBizStatus());
        }
        if (userIdentities.getAcquiredTime() != null) {
            chain.eq(UserIdentities::getAcquiredTime, userIdentities.getAcquiredTime());
        }
        if (userIdentities.getExpiredTime() != null) {
            chain.eq(UserIdentities::getExpiredTime, userIdentities.getExpiredTime());
        }
        if (userIdentities.getSourceType() != null && !userIdentities.getSourceType().isEmpty()) {
            chain.eq(UserIdentities::getSourceType, userIdentities.getSourceType());
        }
        if (userIdentities.getSourceId() != null) {
            chain.eq(UserIdentities::getSourceId, userIdentities.getSourceId());
        }
        if (userIdentities.getStatus() != null && !userIdentities.getStatus().isEmpty()) {
            chain.eq(UserIdentities::getStatus, userIdentities.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserIdentities> page(UserIdentities userIdentities, int pageNum, int pageSize) {
        return selectList(userIdentities).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserIdentities userIdentities, HttpServletResponse response) {
        List<UserIdentities> list = selectList(userIdentities).list();
        ExcelUtil<UserIdentities> util = new ExcelUtil<>(UserIdentities.class);
        util.exportExcel(response, list, "用户身份关系表数据");
    }


}
