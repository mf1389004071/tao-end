package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserTags;
import com.geek.tao.bt10.mapper.UserTagsMapper;
import com.geek.tao.bt10.service.IUserTagsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户与多维标签关联表 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserTagsServiceImpl extends ServiceImpl<UserTagsMapper, UserTags> implements IUserTagsService {

    private QueryChain<UserTags> selectList(UserTags userTags) {
        QueryChain<UserTags> chain = this.queryChain();
        if (userTags.getUserId() != null) {
            chain.eq(UserTags::getUserId, userTags.getUserId());
        }
        if (userTags.getTagId() != null) {
            chain.eq(UserTags::getTagId, userTags.getTagId());
        }
        if (userTags.getWeight() != null) {
            chain.eq(UserTags::getWeight, userTags.getWeight());
        }
        if (userTags.getSource() != null && !userTags.getSource().isEmpty()) {
            chain.eq(UserTags::getSource, userTags.getSource());
        }
        if (userTags.getStatus() != null && !userTags.getStatus().isEmpty()) {
            chain.eq(UserTags::getStatus, userTags.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserTags> page(UserTags userTags, int pageNum, int pageSize) {
        return selectList(userTags).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserTags userTags, HttpServletResponse response) {
        List<UserTags> list = selectList(userTags).list();
        ExcelUtil<UserTags> util = new ExcelUtil<>(UserTags.class);
        util.exportExcel(response, list, "用户与多维标签关联表数据");
    }


}
