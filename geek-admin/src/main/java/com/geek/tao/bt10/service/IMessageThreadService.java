package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.MessageThread;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 私信会话 message_thread 服务
 */
public interface IMessageThreadService extends IService<MessageThread> {

    Page<MessageThread> page(MessageThread query, int pageNum, int pageSize);

    void export(MessageThread query, HttpServletResponse response);

    /**
     * 查找或创建 1v1 会话（userAId 为较小 user_id，userBId 为较大）
     */
    MessageThread ensureThread(Long userId1, Long userId2);

    /**
     * 当前用户参与的会话分页（含对方会话）
     */
    Page<MessageThread> pageForParticipant(Long userId, int pageNum, int pageSize);
}
