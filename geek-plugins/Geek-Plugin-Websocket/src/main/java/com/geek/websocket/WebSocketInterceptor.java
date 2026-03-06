package com.geek.websocket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.geek.common.core.domain.model.LoginUser;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.UriUtil;
import com.geek.framework.web.service.TokenService;

/**
 * WebSocket 握手拦截器
 * 用于处理连接时的 authorization 参数
 * 
 * @author geek
 */
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketInterceptor.class);

    @Value("${token.header}")
    private String header;

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Map<String, String> queryParams = UriUtil.parseQueryParameters(request.getURI());
        attributes.putAll(queryParams);
        String token = queryParams.get(header);
        attributes.put(header, token);
        LoginUser loginUser = tokenService.getLoginUser(token);
        if (StringUtils.isNull(loginUser)) {
            LOGGER.warn("WebSocket 连接认证失败: {}", token);
            return false;
        }
        tokenService.verifyToken(loginUser);
        LOGGER.info("WebSocket 握手成功，远程地址: {}", request.getRemoteAddress());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            LOGGER.error("WebSocket 握手后异常", exception);
        }
    }
}
