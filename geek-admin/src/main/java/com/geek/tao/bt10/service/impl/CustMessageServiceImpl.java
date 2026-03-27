package com.geek.tao.bt10.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geek.common.core.domain.entity.SysUser;
import com.geek.system.service.ISysUserService;
import com.geek.tao.bt10.domain.MessageThread;
import com.geek.tao.bt10.domain.PrivateMessage;
import com.geek.tao.bt10.service.ICustMessageService;
import com.geek.tao.bt10.service.IMessageThreadService;
import com.geek.tao.bt10.service.IPrivateMessageService;
import com.mybatisflex.core.paginate.Page;

/**
 * C 端私信业务实现
 */
@Service
public class CustMessageServiceImpl implements ICustMessageService {

    @Autowired
    private IMessageThreadService messageThreadService;
    @Autowired
    private IPrivateMessageService privateMessageService;
    @Autowired
    private ISysUserService sysUserService;

    private boolean isParticipant(Long me, MessageThread th) {
        return th != null && (me.equals(th.getUserAId()) || me.equals(th.getUserBId()));
    }

    private long otherUserId(Long me, MessageThread th) {
        return th.getUserAId().equals(me) ? th.getUserBId() : th.getUserAId();
    }

    private int unreadForUser(Long me, MessageThread th) {
        if (th.getUserAId().equals(me)) {
            return th.getAUnreadCount() == null ? 0 : th.getAUnreadCount();
        }
        return th.getBUnreadCount() == null ? 0 : th.getBUnreadCount();
    }

    private void setUnreadForUser(Long me, MessageThread th, int value) {
        if (th.getUserAId().equals(me)) {
            th.setAUnreadCount(value);
        } else {
            th.setBUnreadCount(value);
        }
    }

    private void addUnreadForReceiver(Long receiver, MessageThread th, int delta) {
        if (th.getUserAId().equals(receiver)) {
            int v = (th.getAUnreadCount() == null ? 0 : th.getAUnreadCount()) + delta;
            th.setAUnreadCount(Math.max(0, v));
        } else {
            int v = (th.getBUnreadCount() == null ? 0 : th.getBUnreadCount()) + delta;
            th.setBUnreadCount(Math.max(0, v));
        }
    }

    @Override
    public Long ensureThread(Long me, Long targetUserId) {
        if (targetUserId.equals(me)) {
            throw new IllegalArgumentException("不能与自己创建会话");
        }
        MessageThread t = messageThreadService.ensureThread(me, targetUserId);
        return t.getId();
    }

    @Override
    public Page<Map<String, Object>> pageMyThreads(Long me, int pageNum, int pageSize) {
        Page<MessageThread> page = messageThreadService.pageForParticipant(me, pageNum, pageSize);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (MessageThread th : page.getRecords()) {
            long target = otherUserId(me, th);
            int unread = unreadForUser(me, th);
            SysUser u = sysUserService.selectUserById(target);
            String name = u != null ? (u.getNickName() != null && !u.getNickName().isEmpty() ? u.getNickName() : u.getUserName()) : ("用户" + target);
            Map<String, Object> m = new HashMap<>();
            m.put("threadId", String.valueOf(th.getId()));
            m.put("targetUserId", String.valueOf(target));
            m.put("targetUserName", name);
            m.put("lastMessagePreview", th.getLastMessagePreview());
            m.put("lastMessageTime", th.getLastMessageTime());
            m.put("unreadCount", unread);
            rows.add(m);
        }
        Page<Map<String, Object>> out = new Page<>();
        out.setRecords(rows);
        out.setTotalRow(page.getTotalRow());
        out.setPageNumber(page.getPageNumber());
        out.setPageSize(page.getPageSize());
        return out;
    }

    @Override
    public Page<Map<String, Object>> pageMessages(Long me, Long threadId, int pageNum, int pageSize) {
        MessageThread th = messageThreadService.getById(threadId);
        if (th == null || !isParticipant(me, th)) {
            Page<Map<String, Object>> empty = new Page<>();
            empty.setRecords(Collections.emptyList());
            empty.setTotalRow(0);
            return empty;
        }
        Page<PrivateMessage> page = privateMessageService.pageByThread(threadId, pageNum, pageSize);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (PrivateMessage pm : page.getRecords()) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", String.valueOf(pm.getId()));
            m.put("senderId", String.valueOf(pm.getSenderId()));
            m.put("receiverId", String.valueOf(pm.getReceiverId()));
            m.put("content", pm.getContent());
            m.put("sentTime", pm.getSentTime());
            m.put("readTime", pm.getReadTime());
            rows.add(m);
        }
        Page<Map<String, Object>> out = new Page<>();
        out.setRecords(rows);
        out.setTotalRow(page.getTotalRow());
        out.setPageNumber(page.getPageNumber());
        out.setPageSize(page.getPageSize());
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendText(Long me, Long threadId, String content) {
        MessageThread th = messageThreadService.getById(threadId);
        if (th == null || !isParticipant(me, th)) {
            throw new IllegalArgumentException("会话不存在或无权访问");
        }
        long receiver = otherUserId(me, th);
        PrivateMessage pm = new PrivateMessage();
        pm.setThreadId(threadId);
        pm.setSenderId(me);
        pm.setReceiverId(receiver);
        pm.setMessageType("TEXT");
        pm.setContent(content);
        pm.setSentTime(Instant.now());
        privateMessageService.save(pm);
        String preview = content.length() > 200 ? content.substring(0, 200) : content;
        th.setLastMessageId(pm.getId());
        th.setLastMessageTime(pm.getSentTime());
        th.setLastMessagePreview(preview);
        addUnreadForReceiver(receiver, th, +1);
        messageThreadService.updateById(th);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markRead(Long me, Long threadId) {
        MessageThread th = messageThreadService.getById(threadId);
        if (th == null || !isParticipant(me, th)) {
            throw new IllegalArgumentException("会话不存在或无权访问");
        }
        privateMessageService.markReadForReceiver(threadId, me);
        setUnreadForUser(me, th, 0);
        messageThreadService.updateById(th);
    }
}
