package com.geek.ai.service;

import com.geek.ai.client.AiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业务侧统一入口（当前仅默认 provider = yunwu OpenAI 兼容）
 */
@Service
public class AiChatService {

    @Autowired
    private AiClient aiClient;

    public String chat(String systemPrompt, String userPrompt) {
        return aiClient.chat(systemPrompt, userPrompt);
    }

    public String chat(List<Map<String, String>> messages) {
        return aiClient.chat(messages);
    }
}
