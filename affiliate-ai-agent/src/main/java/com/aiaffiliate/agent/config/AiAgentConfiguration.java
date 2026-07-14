package com.aiaffiliate.agent.config;

import com.aiaffiliate.agent.BridgePageGeneratorAgent;
import com.aiaffiliate.agent.KeywordAgent;
import com.aiaffiliate.agent.PinterestContentAgent;
import com.aiaffiliate.agent.ProductAnalyzerAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 创建 ChatModel 及四个类型安全的 LangChain4j Agent Service。 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AiAgentProperties.class)
@ConditionalOnProperty(prefix = "affiliate.ai", name = "enabled", havingValue = "true")
public class AiAgentConfiguration {

    @Bean
    ChatModel affiliateChatModel(AiAgentProperties properties) {
        if (properties.apiKey() == null || properties.apiKey().isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is required when affiliate.ai.enabled=true");
        }
        return OpenAiChatModel.builder()
                .apiKey(properties.apiKey())
                .modelName(properties.modelName())
                .temperature(properties.temperature())
                .build();
    }

    @Bean
    KeywordAgent keywordAgent(ChatModel chatModel) {
        return AiServices.builder(KeywordAgent.class).chatModel(chatModel).build();
    }

    @Bean
    ProductAnalyzerAgent productAnalyzerAgent(ChatModel chatModel) {
        return AiServices.builder(ProductAnalyzerAgent.class).chatModel(chatModel).build();
    }

    @Bean
    BridgePageGeneratorAgent bridgePageGeneratorAgent(ChatModel chatModel) {
        return AiServices.builder(BridgePageGeneratorAgent.class).chatModel(chatModel).build();
    }

    @Bean
    PinterestContentAgent pinterestContentAgent(ChatModel chatModel) {
        return AiServices.builder(PinterestContentAgent.class).chatModel(chatModel).build();
    }
}
