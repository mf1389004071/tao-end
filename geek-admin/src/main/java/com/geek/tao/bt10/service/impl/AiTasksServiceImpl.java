package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.AiTasks;
import com.geek.tao.bt10.mapper.AiTasksMapper;
import com.geek.tao.bt10.service.IAiTasksService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * AI异步任务 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class AiTasksServiceImpl extends ServiceImpl<AiTasksMapper, AiTasks> implements IAiTasksService {

    private QueryChain<AiTasks> selectList(AiTasks aiTasks) {
        QueryChain<AiTasks> chain = this.queryChain();
        if (aiTasks.getTaskType() != null && !aiTasks.getTaskType().isEmpty()) {
            chain.eq(AiTasks::getTaskType, aiTasks.getTaskType());
        }
        if (aiTasks.getBizStatus() != null && !aiTasks.getBizStatus().isEmpty()) {
            chain.eq(AiTasks::getBizStatus, aiTasks.getBizStatus());
        }
        if (aiTasks.getPriority() != null) {
            chain.eq(AiTasks::getPriority, aiTasks.getPriority());
        }
        if (aiTasks.getRelatedType() != null && !aiTasks.getRelatedType().isEmpty()) {
            chain.eq(AiTasks::getRelatedType, aiTasks.getRelatedType());
        }
        if (aiTasks.getRelatedId() != null) {
            chain.eq(AiTasks::getRelatedId, aiTasks.getRelatedId());
        }
        if (aiTasks.getConfig() != null && !aiTasks.getConfig().isEmpty()) {
            chain.eq(AiTasks::getConfig, aiTasks.getConfig());
        }
        if (aiTasks.getResult() != null && !aiTasks.getResult().isEmpty()) {
            chain.eq(AiTasks::getResult, aiTasks.getResult());
        }
        if (aiTasks.getProgressPercentage() != null) {
            chain.eq(AiTasks::getProgressPercentage, aiTasks.getProgressPercentage());
        }
        if (aiTasks.getErrorMessage() != null && !aiTasks.getErrorMessage().isEmpty()) {
            chain.eq(AiTasks::getErrorMessage, aiTasks.getErrorMessage());
        }
        if (aiTasks.getCostAmount() != null) {
            chain.eq(AiTasks::getCostAmount, aiTasks.getCostAmount());
        }
        if (aiTasks.getTokensUsed() != null) {
            chain.eq(AiTasks::getTokensUsed, aiTasks.getTokensUsed());
        }
        if (aiTasks.getModelUsed() != null && !aiTasks.getModelUsed().isEmpty()) {
            chain.eq(AiTasks::getModelUsed, aiTasks.getModelUsed());
        }
        if (aiTasks.getStartTime() != null) {
            chain.eq(AiTasks::getStartTime, aiTasks.getStartTime());
        }
        if (aiTasks.getCompleteTime() != null) {
            chain.eq(AiTasks::getCompleteTime, aiTasks.getCompleteTime());
        }
        if (aiTasks.getStatus() != null && !aiTasks.getStatus().isEmpty()) {
            chain.eq(AiTasks::getStatus, aiTasks.getStatus());
        }
        return chain;
    }

    @Override
    public Page<AiTasks> page(AiTasks aiTasks, int pageNum, int pageSize) {
        return selectList(aiTasks).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(AiTasks aiTasks, HttpServletResponse response) {
        List<AiTasks> list = selectList(aiTasks).list();
        ExcelUtil<AiTasks> util = new ExcelUtil<>(AiTasks.class);
        util.exportExcel(response, list, "AI异步任务数据");
    }


}
