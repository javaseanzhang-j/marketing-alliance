package com.aiaffiliate.api.config;

import com.aiaffiliate.notion.HttpNotionClient;
import com.aiaffiliate.notion.NotionClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/** 按配置启用 Notion HTTP Client，默认关闭以支持零凭证本地启动。 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(NotionProperties.class)
@ConditionalOnProperty(prefix = "affiliate.notion", name = "enabled", havingValue = "true")
public class NotionConfiguration {

    @Bean
    NotionClient notionClient(NotionProperties properties, ObjectMapper objectMapper) {
        if (properties.token() == null || properties.token().isBlank()) {
            throw new IllegalStateException("NOTION_TOKEN is required when affiliate.notion.enabled=true");
        }
        return new HttpNotionClient(
                properties.token(), Duration.ofSeconds(properties.timeoutSeconds()), objectMapper);
    }
}

