package com.geek.websocket;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.geek.common.core.domain.Message;
import com.geek.common.core.domain.model.LoginUser;
import com.geek.common.utils.JSON;

/**
 * websocket 客户端用户集
 * 
 * @author geek
 */
public class WebSocketUsers {
    /**
     * WebSocketUsers 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketUsers.class);

    /**
     * session集
     */
    public static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();
    public static final Map<String, WebSocketSession> USERNAME = new ConcurrentHashMap<>();
    public static final Map<String, LoginUser> LOGINUSER = new ConcurrentHashMap<>();

    public static final int socketMaxOnlineCount = 100;
    private static final Semaphore socketSemaphore = new Semaphore(socketMaxOnlineCount);

    /**
     * 存储用户
     *
     * @param key     唯一键
     * @param session 用户信息
     */
    public static void put(String key, WebSocketSession session, LoginUser loginUser) {
        if (!socketSemaphore.tryAcquire()) {
            throw new RuntimeException("当前在线人数超过限制数：" + socketMaxOnlineCount);
        }
        SESSIONS.put(key, session);
        if (loginUser != null) {
            LOGINUSER.put(key, loginUser);
            USERNAME.put(loginUser.getUsername(), session);
        }
    }

    /**
     * 移出用户
     *
     * @param key 键
     */
    public static boolean remove(String key) {
        LOGGER.info("\n 正在移出用户 - {}", key);
        SESSIONS.remove(key);
        LoginUser loginUser = LOGINUSER.remove(key);
        if (loginUser != null) {
            USERNAME.remove(loginUser.getUsername());
        }
        socketSemaphore.release();
        return true;
    }

    /**
     * 获取在线用户列表
     *
     * @return 返回用户集合
     */
    public static Collection<LoginUser> getUsers() {
        return LOGINUSER.values();
    }

    public static Map<String, WebSocketSession> getSessions() {
        return SESSIONS;
    }

    /**
     * 群发消息文本消息
     *
     * @param message 消息内容
     */
    public static void sendMessageToUsersByText(String message) {
        Collection<WebSocketSession> values = SESSIONS.values();
        for (WebSocketSession value : values) {
            sendMessageToUserByText(value, message);
        }
    }

    /**
     * 发送文本消息
     *
     * @param session WebSocket会话
     * @param message 消息内容
     */
    public static void sendMessageToUserByText(WebSocketSession session, String message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                LOGGER.error("\n[发送消息异常]", e);
            }
        } else {
            LOGGER.info("\n[连接已断开或不存在]");
        }
    }

    /**
     * 发送消息
     *
     * @param session WebSocket会话
     * @param message 消息内容
     */
    public static void sendMessageToUser(WebSocketSession session, Message message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(JSON.toJSONString(message)));
            } catch (IOException e) {
                LOGGER.error("\n[发送消息异常]", e);
            }
        } else {
            LOGGER.info("\n[连接已断开或不存在]");
        }
    }
}
