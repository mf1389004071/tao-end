package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.Tags;
import com.geek.tao.bt10.mapper.TagsMapper;
import com.geek.tao.bt10.service.ITagsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 通用标签定义表 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

    private QueryChain<Tags> selectList(Tags tags) {
        QueryChain<Tags> chain = this.queryChain();
        if (tags.getTagType() != null && !tags.getTagType().isEmpty()) {
            chain.eq(Tags::getTagType, tags.getTagType());
        }
        if (tags.getCode() != null && !tags.getCode().isEmpty()) {
            chain.eq(Tags::getCode, tags.getCode());
        }
        if (tags.getName() != null && !tags.getName().isEmpty()) {
            chain.like(Tags::getName, tags.getName());
        }
        if (tags.getParentId() != null) {
            chain.eq(Tags::getParentId, tags.getParentId());
        }
        if (tags.getOrderNum() != null) {
            chain.eq(Tags::getOrderNum, tags.getOrderNum());
        }
        if (tags.getStatus() != null && !tags.getStatus().isEmpty()) {
            chain.eq(Tags::getStatus, tags.getStatus());
        }
        return chain;
    }

    @Override
    public Page<Tags> page(Tags tags, int pageNum, int pageSize) {
        return selectList(tags).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(Tags tags, HttpServletResponse response) {
        List<Tags> list = selectList(tags).list();
        ExcelUtil<Tags> util = new ExcelUtil<>(Tags.class);
        util.exportExcel(response, list, "通用标签定义表数据");
    }


}
