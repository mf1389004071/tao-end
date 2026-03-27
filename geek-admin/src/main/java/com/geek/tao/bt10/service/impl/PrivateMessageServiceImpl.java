package com.geek.tao.bt10.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.PrivateMessage;
import com.geek.tao.bt10.mapper.PrivateMessageMapper;
import com.geek.tao.bt10.service.IPrivateMessageService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 私信消息 服务实现
 */
@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements IPrivateMessageService {

    private QueryChain<PrivateMessage> selectList(PrivateMessage query) {
        QueryChain<PrivateMessage> chain = this.queryChain();
        if (query.getThreadId() != null) {
            chain.eq(PrivateMessage::getThreadId, query.getThreadId());
        }
        if (query.getSenderId() != null) {
            chain.eq(PrivateMessage::getSenderId, query.getSenderId());
        }
        if (query.getReceiverId() != null) {
            chain.eq(PrivateMessage::getReceiverId, query.getReceiverId());
        }
        if (query.getMessageType() != null && !query.getMessageType().isEmpty()) {
            chain.eq(PrivateMessage::getMessageType, query.getMessageType());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            chain.eq(PrivateMessage::getStatus, query.getStatus());
        }
        return chain;
    }

    @Override
    public Page<PrivateMessage> page(PrivateMessage query, int pageNum, int pageSize) {
        return selectList(query).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(PrivateMessage query, HttpServletResponse response) {
        List<PrivateMessage> list = selectList(query).list();
        ExcelUtil<PrivateMessage> util = new ExcelUtil<>(PrivateMessage.class);
        util.exportExcel(response, list, "私信消息数据");
    }

    @Override
    public Page<PrivateMessage> pageByThread(Long threadId, int pageNum, int pageSize) {
        QueryWrapper qw = QueryWrapper.create()
            .from(PrivateMessage.class)
            .eq(PrivateMessage::getThreadId, threadId)
            .orderBy(PrivateMessage::getSentTime, false);
        return getMapper().paginate(pageNum, pageSize, qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markReadForReceiver(Long threadId, Long receiverId) {
        QueryWrapper qw = QueryWrapper.create()
            .from(PrivateMessage.class)
            .eq(PrivateMessage::getThreadId, threadId)
            .eq(PrivateMessage::getReceiverId, receiverId)
            .isNull(PrivateMessage::getReadTime);
        List<PrivateMessage> list = getMapper().selectListByQuery(qw);
        Instant now = Instant.now();
        for (PrivateMessage pm : list) {
            pm.setReadTime(now);
            updateById(pm);
        }
        return list.size();
    }
}
