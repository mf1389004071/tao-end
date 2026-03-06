package com.geek.redis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.annotation.Anonymous;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.domain.Message;
import com.geek.common.utils.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 缓存监控
 * 
 * @author geek
 */
@Tag(name = "缓存监控")
@RestController
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@ConditionalOnProperty(prefix = "spring.cache", name = { "type" }, havingValue = "redis", matchIfMissing = false)
@RequestMapping("/monitor/cache")
public class RedisCacheController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Operation(summary = "获取缓存信息")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Map<String, Object> result = new HashMap<>(3);

        Properties info = (Properties) redisTemplate
                .execute((RedisCallback<Object>) connection -> connection.commands().info());
        Properties commandStats = (Properties) redisTemplate
                .execute((RedisCallback<Object>) connection -> connection.commands().info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.commands().dbSize());
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return AjaxResult.success(result);
    }

    @Anonymous
    @GetMapping("/test")
    public String getMethodName() {
        redisTemplate.convertAndSend("order.channel", Message.builder()
                .payload(Map.of("message", "你好"))
                .receiver("接收者")
                .sender("发送者")
                .build());
        return new String("ok");
    }

}
