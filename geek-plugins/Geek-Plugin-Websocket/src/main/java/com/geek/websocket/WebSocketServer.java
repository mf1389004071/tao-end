package com.geek.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.geek.common.core.domain.Message;
import com.geek.common.core.domain.Message.MessageBuilder;
import com.geek.common.core.domain.model.LoginUser;
import com.geek.common.enums.MessageType;
import com.geek.common.utils.JSON;
import com.geek.framework.web.service.TokenService;

/**
 * websocket 消息处理
 * 
 * @author geek
 */
@Component
public class WebSocketServer extends TextWebSocketHandler {

    @Autowired
    private TokenService tokenService;

    @Value("${token.header}")
    private String header;
    /**
     * WebSocketServer 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 连接建立成功调用的方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        MessageBuilder message = Message.builder();
        message.sender("system");
        try {
            // 获取 authorization 信息
            String authorization = (String) session.getAttributes().get(header);
            LoginUser loginUser = tokenService.getLoginUser(authorization);
            session.getAttributes().put("USER", loginUser);
            // 添加用户
            WebSocketUsers.put(session.getId(), session, loginUser);
            LOGGER.info("\n 建立连接 - {}", session.getId());
            LOGGER.info("\n 当前人数 - {}", WebSocketUsers.getUsers().size());
            message.content("连接成功,你好" + loginUser.getUsername());
            WebSocketUsers.sendMessageToUser(session, message.build());
        } catch (Exception e) {
            // 未获取到信号量
            LOGGER.error("\n 当前在线人数超过限制数- {}", WebSocketUsers.socketMaxOnlineCount);
            message.content("当前在线人数超过限制数：" + WebSocketUsers.socketMaxOnlineCount);
            WebSocketUsers.sendMessageToUser(session, message.build());
            session.close();
        }
    }

    /**
     * 连接关闭时处理
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("\n 关闭连接 - {}, 状态: {}", session.getId(), status);
        WebSocketUsers.remove(session.getId());
    }

    /**
     * 抛出异常时处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            // 关闭连接
            session.close();
        }
        String sessionId = session.getId();
        LOGGER.info("\n 连接异常 - {}", sessionId);
        LOGGER.info("\n 异常信息 - {}", exception);
        // 移出用户
        WebSocketUsers.remove(sessionId);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Message msg = JSON.parseObject(payload, Message.class);
        WebSocketSession receiver = WebSocketUsers.USERNAME.get(msg.getReceiver());
        if (msg.getType().equals(MessageType.ASYNC_MESSAGE)) {
            WebSocketUsers.sendMessageToUser(session, msg);
        } else {
            if (receiver == null) {
                LOGGER.error("\n 无法找到接收者 - {}", msg.getReceiver());
                return;
            }
            WebSocketUsers.sendMessageToUser(receiver, msg);
        }
    }
}
