package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.AiMeetingTranscripts;
import com.geek.tao.bt10.mapper.AiMeetingTranscriptsMapper;
import com.geek.tao.bt10.service.IAiMeetingTranscriptsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 音视频转写与AI摘要 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class AiMeetingTranscriptsServiceImpl extends ServiceImpl<AiMeetingTranscriptsMapper, AiMeetingTranscripts> implements IAiMeetingTranscriptsService {

    private QueryChain<AiMeetingTranscripts> selectList(AiMeetingTranscripts aiMeetingTranscripts) {
        QueryChain<AiMeetingTranscripts> chain = this.queryChain();
        if (aiMeetingTranscripts.getEventId() != null) {
            chain.eq(AiMeetingTranscripts::getEventId, aiMeetingTranscripts.getEventId());
        }
        if (aiMeetingTranscripts.getSessionId() != null) {
            chain.eq(AiMeetingTranscripts::getSessionId, aiMeetingTranscripts.getSessionId());
        }
        if (aiMeetingTranscripts.getFileId() != null) {
            chain.eq(AiMeetingTranscripts::getFileId, aiMeetingTranscripts.getFileId());
        }
        if (aiMeetingTranscripts.getTranscriptJson() != null && !aiMeetingTranscripts.getTranscriptJson().isEmpty()) {
            chain.eq(AiMeetingTranscripts::getTranscriptJson, aiMeetingTranscripts.getTranscriptJson());
        }
        if (aiMeetingTranscripts.getFullText() != null && !aiMeetingTranscripts.getFullText().isEmpty()) {
            chain.eq(AiMeetingTranscripts::getFullText, aiMeetingTranscripts.getFullText());
        }
        if (aiMeetingTranscripts.getSummary() != null && !aiMeetingTranscripts.getSummary().isEmpty()) {
            chain.eq(AiMeetingTranscripts::getSummary, aiMeetingTranscripts.getSummary());
        }
        if (aiMeetingTranscripts.getKeyPoints() != null && !aiMeetingTranscripts.getKeyPoints().isEmpty()) {
            chain.eq(AiMeetingTranscripts::getKeyPoints, aiMeetingTranscripts.getKeyPoints());
        }
        if (aiMeetingTranscripts.getActionItems() != null && !aiMeetingTranscripts.getActionItems().isEmpty()) {
            chain.eq(AiMeetingTranscripts::getActionItems, aiMeetingTranscripts.getActionItems());
        }
        if (aiMeetingTranscripts.getStatus() != null && !aiMeetingTranscripts.getStatus().isEmpty()) {
            chain.eq(AiMeetingTranscripts::getStatus, aiMeetingTranscripts.getStatus());
        }
        return chain;
    }

    @Override
    public Page<AiMeetingTranscripts> page(AiMeetingTranscripts aiMeetingTranscripts, int pageNum, int pageSize) {
        return selectList(aiMeetingTranscripts).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(AiMeetingTranscripts aiMeetingTranscripts, HttpServletResponse response) {
        List<AiMeetingTranscripts> list = selectList(aiMeetingTranscripts).list();
        ExcelUtil<AiMeetingTranscripts> util = new ExcelUtil<>(AiMeetingTranscripts.class);
        util.exportExcel(response, list, "音视频转写与AI摘要数据");
    }


}
