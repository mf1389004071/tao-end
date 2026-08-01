package com.geek.ai.client;

import java.util.List;
import java.util.Map;

public interface AiClient {

    /**
     * OpenAI 兼容 chat/completions
     * @return assistant 文本内容
     */
    String chat(String systemPrompt, String userPrompt);

    String chat(List<Map<String, String>> messages);
}
