package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.KnowledgeComment;
import com.geek.tao.bt10.mapper.KnowledgeCommentMapper;
import com.geek.tao.bt10.service.IKnowledgeCommentService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识内容评论与回复，支持楼中楼与置顶 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-08
 */
@Service
public class KnowledgeCommentServiceImpl extends ServiceImpl<KnowledgeCommentMapper, KnowledgeComment> implements IKnowledgeCommentService {

    private QueryChain<KnowledgeComment> selectList(KnowledgeComment knowledgeComment) {
        QueryChain<KnowledgeComment> chain = this.queryChain();
        if (knowledgeComment.getContentId() != null) {
            chain.eq(KnowledgeComment::getContentId, knowledgeComment.getContentId());
        }
        if (knowledgeComment.getUserId() != null) {
            chain.eq(KnowledgeComment::getUserId, knowledgeComment.getUserId());
        }
        if (knowledgeComment.getParentId() != null) {
            chain.eq(KnowledgeComment::getParentId, knowledgeComment.getParentId());
        }
        if (knowledgeComment.getContent() != null && !knowledgeComment.getContent().isEmpty()) {
            chain.eq(KnowledgeComment::getContent, knowledgeComment.getContent());
        }
        if (knowledgeComment.getLikeCount() != null) {
            chain.eq(KnowledgeComment::getLikeCount, knowledgeComment.getLikeCount());
        }
        if (knowledgeComment.getIsPinned() != null) {
            chain.eq(KnowledgeComment::getIsPinned, knowledgeComment.getIsPinned());
        }
        if (knowledgeComment.getBizStatus() != null && !knowledgeComment.getBizStatus().isEmpty()) {
            chain.eq(KnowledgeComment::getBizStatus, knowledgeComment.getBizStatus());
        }
        if (knowledgeComment.getStatus() != null && !knowledgeComment.getStatus().isEmpty()) {
            chain.eq(KnowledgeComment::getStatus, knowledgeComment.getStatus());
        }
        return chain;
    }

    @Override
    public Page<KnowledgeComment> page(KnowledgeComment knowledgeComment, int pageNum, int pageSize) {
        return selectList(knowledgeComment).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(KnowledgeComment knowledgeComment, HttpServletResponse response) {
        List<KnowledgeComment> list = selectList(knowledgeComment).list();
        ExcelUtil<KnowledgeComment> util = new ExcelUtil<>(KnowledgeComment.class);
        util.exportExcel(response, list, "知识内容评论与回复，支持楼中楼与置顶数据");
    }


}
