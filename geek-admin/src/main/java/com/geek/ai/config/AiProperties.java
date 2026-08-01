package com.geek.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "geek.ai")
public class AiProperties {

    private boolean enabled = true;
    private String defaultProvider = "yunwu";
    private Map<String, Provider> providers = new LinkedHashMap<>();

    @Data
    public static class Provider {
        /** 如 https://yunwu.ai/v1 （不要带 /chat/completions） */
        private String baseUrl;
        private String apiKey;
        private String model = "gpt-4o-mini";
        private int timeoutSeconds = 90;
    }

    public Provider resolve() {
        if (providers == null || providers.isEmpty()) {
            return null;
        }
        Provider p = providers.get(defaultProvider);
        if (p == null) {
            p = providers.values().iterator().next();
        }
        return p;
    }
}
