package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.KnowledgeCategory;
import com.geek.tao.bt10.mapper.KnowledgeCategoryMapper;
import com.geek.tao.bt10.service.IKnowledgeCategoryService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识库分类 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class KnowledgeCategoryServiceImpl extends ServiceImpl<KnowledgeCategoryMapper, KnowledgeCategory> implements IKnowledgeCategoryService {

    private QueryChain<KnowledgeCategory> selectList(KnowledgeCategory knowledgeCategory) {
        QueryChain<KnowledgeCategory> chain = this.queryChain();
        if (knowledgeCategory.getName() != null && !knowledgeCategory.getName().isEmpty()) {
            chain.like(KnowledgeCategory::getName, knowledgeCategory.getName());
        }
        if (knowledgeCategory.getSlug() != null && !knowledgeCategory.getSlug().isEmpty()) {
            chain.eq(KnowledgeCategory::getSlug, knowledgeCategory.getSlug());
        }
        if (knowledgeCategory.getParentId() != null) {
            chain.eq(KnowledgeCategory::getParentId, knowledgeCategory.getParentId());
        }
        if (knowledgeCategory.getOrderNum() != null) {
            chain.eq(KnowledgeCategory::getOrderNum, knowledgeCategory.getOrderNum());
        }
        if (knowledgeCategory.getStatus() != null && !knowledgeCategory.getStatus().isEmpty()) {
            chain.eq(KnowledgeCategory::getStatus, knowledgeCategory.getStatus());
        }
        if (knowledgeCategory.getIcon() != null && !knowledgeCategory.getIcon().isEmpty()) {
            chain.eq(KnowledgeCategory::getIcon, knowledgeCategory.getIcon());
        }
        if (knowledgeCategory.getColor() != null && !knowledgeCategory.getColor().isEmpty()) {
            chain.eq(KnowledgeCategory::getColor, knowledgeCategory.getColor());
        }
        if (knowledgeCategory.getPermissionLevel() != null) {
            chain.eq(KnowledgeCategory::getPermissionLevel, knowledgeCategory.getPermissionLevel());
        }
        if (knowledgeCategory.getText1() != null && !knowledgeCategory.getText1().isEmpty()) {
            chain.eq(KnowledgeCategory::getText1, knowledgeCategory.getText1());
        }
        if (knowledgeCategory.getText2() != null && !knowledgeCategory.getText2().isEmpty()) {
            chain.eq(KnowledgeCategory::getText2, knowledgeCategory.getText2());
        }
        if (knowledgeCategory.getText3() != null && !knowledgeCategory.getText3().isEmpty()) {
            chain.eq(KnowledgeCategory::getText3, knowledgeCategory.getText3());
        }
        if (knowledgeCategory.getJsonData() != null && !knowledgeCategory.getJsonData().isEmpty()) {
            chain.eq(KnowledgeCategory::getJsonData, knowledgeCategory.getJsonData());
        }
        return chain;
    }

    @Override
    public Page<KnowledgeCategory> page(KnowledgeCategory knowledgeCategory, int pageNum, int pageSize) {
        return selectList(knowledgeCategory).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(KnowledgeCategory knowledgeCategory, HttpServletResponse response) {
        List<KnowledgeCategory> list = selectList(knowledgeCategory).list();
        ExcelUtil<KnowledgeCategory> util = new ExcelUtil<>(KnowledgeCategory.class);
        util.exportExcel(response, list, "知识库分类数据");
    }


}
