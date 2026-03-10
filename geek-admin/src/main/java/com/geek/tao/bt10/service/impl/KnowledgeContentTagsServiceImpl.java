package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.KnowledgeContentTags;
import com.geek.tao.bt10.mapper.KnowledgeContentTagsMapper;
import com.geek.tao.bt10.service.IKnowledgeContentTagsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识内容与标签多对多关联 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class KnowledgeContentTagsServiceImpl extends ServiceImpl<KnowledgeContentTagsMapper, KnowledgeContentTags> implements IKnowledgeContentTagsService {

    private QueryChain<KnowledgeContentTags> selectList(KnowledgeContentTags knowledgeContentTags) {
        QueryChain<KnowledgeContentTags> chain = this.queryChain();
        if (knowledgeContentTags.getContentId() != null) {
            chain.eq(KnowledgeContentTags::getContentId, knowledgeContentTags.getContentId());
        }
        if (knowledgeContentTags.getTagId() != null) {
            chain.eq(KnowledgeContentTags::getTagId, knowledgeContentTags.getTagId());
        }
        if (knowledgeContentTags.getOrderNum() != null) {
            chain.eq(KnowledgeContentTags::getOrderNum, knowledgeContentTags.getOrderNum());
        }
        return chain;
    }

    @Override
    public Page<KnowledgeContentTags> page(KnowledgeContentTags knowledgeContentTags, int pageNum, int pageSize) {
        return selectList(knowledgeContentTags).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(KnowledgeContentTags knowledgeContentTags, HttpServletResponse response) {
        List<KnowledgeContentTags> list = selectList(knowledgeContentTags).list();
        ExcelUtil<KnowledgeContentTags> util = new ExcelUtil<>(KnowledgeContentTags.class);
        util.exportExcel(response, list, "知识内容与标签多对多关联数据");
    }


}
