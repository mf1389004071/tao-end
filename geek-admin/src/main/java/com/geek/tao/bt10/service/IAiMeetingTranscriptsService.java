package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.AiMeetingTranscripts;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 音视频转写与AI摘要 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IAiMeetingTranscriptsService extends IService<AiMeetingTranscripts> {

    /**
     * 分页查询音视频转写与AI摘要
     *
     * @param aiMeetingTranscripts 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<AiMeetingTranscripts> page(AiMeetingTranscripts aiMeetingTranscripts, int pageNum, int pageSize);

    /**
     * 导出音视频转写与AI摘要
     *
     * @param aiMeetingTranscripts 查询条件
     * @param response 响应
     */
    void export(AiMeetingTranscripts aiMeetingTranscripts, HttpServletResponse response);


}
