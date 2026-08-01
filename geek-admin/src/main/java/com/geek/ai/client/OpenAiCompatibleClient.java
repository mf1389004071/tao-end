package com.geek.ai.client;

import com.geek.ai.config.AiProperties;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.http.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAI Compatible Client（对接 yunwu.ai / DeepSeek 等）
 */
@Component
public class OpenAiCompatibleClient implements AiClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAiCompatibleClient.class);

    private final AiProperties aiProperties;
    private final ObjectMapper objectMapper;

    public OpenAiCompatibleClient(AiProperties aiProperties, ObjectMapper objectMapper) {
        this.aiProperties = aiProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public String chat(String systemPrompt, String userPrompt) {
        List<Map<String, String>> messages = new ArrayList<>();
        if (StringUtils.isNotEmpty(systemPrompt)) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userPrompt == null ? "" : userPrompt));
        return chat(messages);
    }

    @Override
    public String chat(List<Map<String, String>> messages) {
        if (!aiProperties.isEnabled()) {
            throw new ServiceException("AI 未启用（geek.ai.enabled=false）");
        }
        AiProperties.Provider provider = aiProperties.resolve();
        if (provider == null || StringUtils.isEmpty(provider.getBaseUrl())) {
            throw new ServiceException("未配置 geek.ai.providers");
        }
        if (StringUtils.isEmpty(provider.getApiKey())) {
            throw new ServiceException("未配置 AI api-key（环境变量 YUNWU_API_KEY）");
        }

        String base = provider.getBaseUrl().replaceAll("/+$", "");
        String url = base.endsWith("/v1") ? base + "/chat/completions" : base + "/v1/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", provider.getModel());
        body.put("messages", messages);
        body.put("temperature", 0.7);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + provider.getApiKey());
        headers.put("Content-Type", "application/json");

        String resp = HttpUtils.postJson(url, body, headers);
        if (StringUtils.isEmpty(resp)) {
            throw new ServiceException("AI 调用失败：空响应");
        }
        try {
            JsonNode root = objectMapper.readTree(resp);
            if (root.has("error")) {
                String msg = root.path("error").path("message").asText(root.path("error").asText("unknown"));
                throw new ServiceException("AI 错误: " + msg);
            }
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.isNull()) {
                log.warn("AI 响应无 content: {}", resp.length() > 500 ? resp.substring(0, 500) : resp);
                throw new ServiceException("AI 响应格式异常");
            }
            return content.asText();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("解析 AI 响应失败", e);
            throw new ServiceException("解析 AI 响应失败");
        }
    }
}
