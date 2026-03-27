package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.PrivateMessage;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 私信消息 private_message 服务
 */
public interface IPrivateMessageService extends IService<PrivateMessage> {

    Page<PrivateMessage> page(PrivateMessage query, int pageNum, int pageSize);

    void export(PrivateMessage query, HttpServletResponse response);

    /** 某会话消息分页（按发送时间倒序） */
    Page<PrivateMessage> pageByThread(Long threadId, int pageNum, int pageSize);

    /** 将接收方未读消息标记为已读，返回更新条数 */
    int markReadForReceiver(Long threadId, Long receiverId);
}
