package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.KnowledgeAction;
import com.geek.tao.bt10.mapper.KnowledgeActionMapper;
import com.geek.tao.bt10.service.IKnowledgeActionService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户对知识内容的行为记录 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class KnowledgeActionServiceImpl extends ServiceImpl<KnowledgeActionMapper, KnowledgeAction> implements IKnowledgeActionService {

    private QueryChain<KnowledgeAction> selectList(KnowledgeAction knowledgeAction) {
        QueryChain<KnowledgeAction> chain = this.queryChain();
        if (knowledgeAction.getContentId() != null) {
            chain.eq(KnowledgeAction::getContentId, knowledgeAction.getContentId());
        }
        if (knowledgeAction.getActionType() != null && !knowledgeAction.getActionType().isEmpty()) {
            chain.eq(KnowledgeAction::getActionType, knowledgeAction.getActionType());
        }
        if (knowledgeAction.getUserId() != null) {
            chain.eq(KnowledgeAction::getUserId, knowledgeAction.getUserId());
        }
        if (knowledgeAction.getStatus() != null && !knowledgeAction.getStatus().isEmpty()) {
            chain.eq(KnowledgeAction::getStatus, knowledgeAction.getStatus());
        }
        return chain;
    }

    @Override
    public Page<KnowledgeAction> page(KnowledgeAction knowledgeAction, int pageNum, int pageSize) {
        return selectList(knowledgeAction).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(KnowledgeAction knowledgeAction, HttpServletResponse response) {
        List<KnowledgeAction> list = selectList(knowledgeAction).list();
        ExcelUtil<KnowledgeAction> util = new ExcelUtil<>(KnowledgeAction.class);
        util.exportExcel(response, list, "用户对知识内容的行为记录数据");
    }


}
