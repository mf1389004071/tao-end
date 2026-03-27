package com.geek.tao.bt10.service;

import java.util.Map;

import com.mybatisflex.core.paginate.Page;

/**
 * C 端私信业务（组合 message_thread / private_message）
 */
public interface ICustMessageService {

    Long ensureThread(Long me, Long targetUserId);

    Page<Map<String, Object>> pageMyThreads(Long me, int pageNum, int pageSize);

    Page<Map<String, Object>> pageMessages(Long me, Long threadId, int pageNum, int pageSize);

    void sendText(Long me, Long threadId, String content);

    void markRead(Long me, Long threadId);
}
