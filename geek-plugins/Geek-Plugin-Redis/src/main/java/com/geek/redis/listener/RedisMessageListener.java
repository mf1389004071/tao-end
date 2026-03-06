package com.geek.redis.listener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.geek.common.core.domain.Message;
import com.geek.common.utils.JSON;
import com.geek.redis.annotation.RedisListener;

import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RedisListener("order.channel")
public class RedisMessageListener implements MessageListener {
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        System.out.println("收到消息：" + message.toString());
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("收到消息 - 频道: {}, 内容: {}", channel, body);
        Message msg = JSON.parseObject(body, Message.class);
        messageQueue.offer(msg);
    }

    public RedisMessageListener() {
        scheduler.scheduleAtFixedRate(this::processMessages, 0, 1, TimeUnit.SECONDS);
    }

    private void processMessages() {
        List<Message> messages = new ArrayList<>();
        messageQueue.drainTo(messages, 30);
        if (!messages.isEmpty()) {
            log.info("批量处理消息: {}", messages);
        }
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }
}
