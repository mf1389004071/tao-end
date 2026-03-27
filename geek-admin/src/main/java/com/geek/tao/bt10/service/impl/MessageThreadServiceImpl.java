package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.MessageThread;
import com.geek.tao.bt10.mapper.MessageThreadMapper;
import com.geek.tao.bt10.service.IMessageThreadService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 私信会话 服务实现
 */
@Service
public class MessageThreadServiceImpl extends ServiceImpl<MessageThreadMapper, MessageThread> implements IMessageThreadService {

    private QueryChain<MessageThread> selectList(MessageThread query) {
        QueryChain<MessageThread> chain = this.queryChain();
        if (query.getUserAId() != null) {
            chain.eq(MessageThread::getUserAId, query.getUserAId());
        }
        if (query.getUserBId() != null) {
            chain.eq(MessageThread::getUserBId, query.getUserBId());
        }
        if (query.getLastMessageId() != null) {
            chain.eq(MessageThread::getLastMessageId, query.getLastMessageId());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            chain.eq(MessageThread::getStatus, query.getStatus());
        }
        return chain;
    }

    @Override
    public Page<MessageThread> page(MessageThread query, int pageNum, int pageSize) {
        return selectList(query).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(MessageThread query, HttpServletResponse response) {
        List<MessageThread> list = selectList(query).list();
        ExcelUtil<MessageThread> util = new ExcelUtil<>(MessageThread.class);
        util.exportExcel(response, list, "私信会话数据");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageThread ensureThread(Long userId1, Long userId2) {
        long a = Math.min(userId1, userId2);
        long b = Math.max(userId1, userId2);
        MessageThread t = this.queryChain()
            .eq(MessageThread::getUserAId, a)
            .eq(MessageThread::getUserBId, b)
            .one();
        if (t == null) {
            t = new MessageThread();
            t.setUserAId(a);
            t.setUserBId(b);
            t.setAUnreadCount(0);
            t.setBUnreadCount(0);
            this.save(t);
        }
        return t;
    }

    @Override
    public Page<MessageThread> pageForParticipant(Long userId, int pageNum, int pageSize) {
        QueryWrapper qw = QueryWrapper.create()
            .from(MessageThread.class)
            .where("user_a_id = ? OR user_b_id = ?", userId, userId)
            .orderBy("last_message_time", false);
        return getMapper().paginate(pageNum, pageSize, qw);
    }
}
