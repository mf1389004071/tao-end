package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.KnowledgeContent;
import com.geek.tao.bt10.mapper.KnowledgeContentMapper;
import com.geek.tao.bt10.service.IKnowledgeContentService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识库内容 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class KnowledgeContentServiceImpl extends ServiceImpl<KnowledgeContentMapper, KnowledgeContent> implements IKnowledgeContentService {

    private QueryChain<KnowledgeContent> selectList(KnowledgeContent knowledgeContent) {
        QueryChain<KnowledgeContent> chain = this.queryChain();
        if (knowledgeContent.getTitle() != null && !knowledgeContent.getTitle().isEmpty()) {
            chain.eq(KnowledgeContent::getTitle, knowledgeContent.getTitle());
        }
        if (knowledgeContent.getSubtitle() != null && !knowledgeContent.getSubtitle().isEmpty()) {
            chain.eq(KnowledgeContent::getSubtitle, knowledgeContent.getSubtitle());
        }
        if (knowledgeContent.getContent() != null && !knowledgeContent.getContent().isEmpty()) {
            chain.eq(KnowledgeContent::getContent, knowledgeContent.getContent());
        }
        if (knowledgeContent.getContentType() != null && !knowledgeContent.getContentType().isEmpty()) {
            chain.eq(KnowledgeContent::getContentType, knowledgeContent.getContentType());
        }
        if (knowledgeContent.getCategoryId() != null) {
            chain.eq(KnowledgeContent::getCategoryId, knowledgeContent.getCategoryId());
        }
        if (knowledgeContent.getAuthorId() != null) {
            chain.eq(KnowledgeContent::getAuthorId, knowledgeContent.getAuthorId());
        }
        if (knowledgeContent.getFounderId() != null) {
            chain.eq(KnowledgeContent::getFounderId, knowledgeContent.getFounderId());
        }
        if (knowledgeContent.getKeyContributors() != null && !knowledgeContent.getKeyContributors().isEmpty()) {
            chain.eq(KnowledgeContent::getKeyContributors, knowledgeContent.getKeyContributors());
        }
        if (knowledgeContent.getManagerId() != null) {
            chain.eq(KnowledgeContent::getManagerId, knowledgeContent.getManagerId());
        }
        if (knowledgeContent.getPromotionalText() != null && !knowledgeContent.getPromotionalText().isEmpty()) {
            chain.eq(KnowledgeContent::getPromotionalText, knowledgeContent.getPromotionalText());
        }
        if (knowledgeContent.getCoreValues() != null && !knowledgeContent.getCoreValues().isEmpty()) {
            chain.eq(KnowledgeContent::getCoreValues, knowledgeContent.getCoreValues());
        }
        if (knowledgeContent.getTags() != null && !knowledgeContent.getTags().isEmpty()) {
            chain.eq(KnowledgeContent::getTags, knowledgeContent.getTags());
        }
        if (knowledgeContent.getBizStatus() != null && !knowledgeContent.getBizStatus().isEmpty()) {
            chain.eq(KnowledgeContent::getBizStatus, knowledgeContent.getBizStatus());
        }
        if (knowledgeContent.getPublishTime() != null) {
            chain.eq(KnowledgeContent::getPublishTime, knowledgeContent.getPublishTime());
        }
        if (knowledgeContent.getViewCount() != null) {
            chain.eq(KnowledgeContent::getViewCount, knowledgeContent.getViewCount());
        }
        if (knowledgeContent.getLikeCount() != null) {
            chain.eq(KnowledgeContent::getLikeCount, knowledgeContent.getLikeCount());
        }
        if (knowledgeContent.getCommentCount() != null) {
            chain.eq(KnowledgeContent::getCommentCount, knowledgeContent.getCommentCount());
        }
        if (knowledgeContent.getShareCount() != null) {
            chain.eq(KnowledgeContent::getShareCount, knowledgeContent.getShareCount());
        }
        if (knowledgeContent.getCollectCount() != null) {
            chain.eq(KnowledgeContent::getCollectCount, knowledgeContent.getCollectCount());
        }
        if (knowledgeContent.getSeoTitle() != null && !knowledgeContent.getSeoTitle().isEmpty()) {
            chain.eq(KnowledgeContent::getSeoTitle, knowledgeContent.getSeoTitle());
        }
        if (knowledgeContent.getSeoDescription() != null && !knowledgeContent.getSeoDescription().isEmpty()) {
            chain.eq(KnowledgeContent::getSeoDescription, knowledgeContent.getSeoDescription());
        }
        if (knowledgeContent.getSeoKeywords() != null && !knowledgeContent.getSeoKeywords().isEmpty()) {
            chain.eq(KnowledgeContent::getSeoKeywords, knowledgeContent.getSeoKeywords());
        }
        if (knowledgeContent.getSourceFrom() != null && !knowledgeContent.getSourceFrom().isEmpty()) {
            chain.eq(KnowledgeContent::getSourceFrom, knowledgeContent.getSourceFrom());
        }
        if (knowledgeContent.getDifficultyLevel() != null && !knowledgeContent.getDifficultyLevel().isEmpty()) {
            chain.eq(KnowledgeContent::getDifficultyLevel, knowledgeContent.getDifficultyLevel());
        }
        if (knowledgeContent.getAiSummary() != null && !knowledgeContent.getAiSummary().isEmpty()) {
            chain.eq(KnowledgeContent::getAiSummary, knowledgeContent.getAiSummary());
        }
        if (knowledgeContent.getText1() != null && !knowledgeContent.getText1().isEmpty()) {
            chain.eq(KnowledgeContent::getText1, knowledgeContent.getText1());
        }
        if (knowledgeContent.getText2() != null && !knowledgeContent.getText2().isEmpty()) {
            chain.eq(KnowledgeContent::getText2, knowledgeContent.getText2());
        }
        if (knowledgeContent.getText3() != null && !knowledgeContent.getText3().isEmpty()) {
            chain.eq(KnowledgeContent::getText3, knowledgeContent.getText3());
        }
        if (knowledgeContent.getJsonData() != null && !knowledgeContent.getJsonData().isEmpty()) {
            chain.eq(KnowledgeContent::getJsonData, knowledgeContent.getJsonData());
        }
        if (knowledgeContent.getStatus() != null && !knowledgeContent.getStatus().isEmpty()) {
            chain.eq(KnowledgeContent::getStatus, knowledgeContent.getStatus());
        }
        return chain;
    }

    @Override
    public Page<KnowledgeContent> page(KnowledgeContent knowledgeContent, int pageNum, int pageSize) {
        return selectList(knowledgeContent).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(KnowledgeContent knowledgeContent, HttpServletResponse response) {
        List<KnowledgeContent> list = selectList(knowledgeContent).list();
        ExcelUtil<KnowledgeContent> util = new ExcelUtil<>(KnowledgeContent.class);
        util.exportExcel(response, list, "知识库内容数据");
    }


}
