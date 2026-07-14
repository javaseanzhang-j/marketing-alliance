package com.aiaffiliate.agent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** OpenAI ChatModel 的外部化配置。 */
@ConfigurationProperties(prefix = "affiliate.ai")
public record AiAgentProperties(
        boolean enabled,
        String apiKey,
        String modelName,
        double temperature) {

    public AiAgentProperties {
        modelName = modelName == null || modelName.isBlank() ? "gpt-4.1-mini" : modelName;
        if (temperature < 0 || temperature > 2) {
            throw new IllegalArgumentException("temperature must be between 0 and 2");
        }
    }
}

