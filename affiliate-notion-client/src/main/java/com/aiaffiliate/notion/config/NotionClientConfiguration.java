package com.aiaffiliate.notion.config;

import com.aiaffiliate.notion.client.NotionClient;
import com.aiaffiliate.notion.client.RestClientNotionClient;
import com.aiaffiliate.notion.retry.NotionRetryPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/** Notion RestClient 自动配置。Authorization Header 从不写入应用日志。 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(NotionProperties.class)
@ConditionalOnProperty(prefix = "notion", name = "enabled", havingValue = "true")
public class NotionClientConfiguration {
    @Bean RestClient notionRestClient(NotionProperties properties) {
        requireSecret(properties.token(), "NOTION_TOKEN");
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(
                java.net.http.HttpClient.newBuilder().connectTimeout(properties.connectTimeout()).build());
        factory.setReadTimeout(properties.readTimeout());
        return RestClient.builder().baseUrl(properties.baseUrl()).requestFactory(factory)
                .defaultHeader("Authorization", "Bearer " + properties.token())
                .defaultHeader("Notion-Version", properties.apiVersion())
                .defaultHeader("Content-Type", "application/json").build();
    }
    @Bean NotionRetryPolicy notionRetryPolicy(NotionProperties properties) { return new NotionRetryPolicy(properties.maxRetries()); }
    @Bean NotionClient notionClient(RestClient client, ObjectMapper mapper, NotionRetryPolicy retryPolicy) {
        return new RestClientNotionClient(client, mapper, retryPolicy);
    }
    private static void requireSecret(String value, String name) {
        if (value == null || value.isBlank()) throw new IllegalStateException(name + " is required when notion.enabled=true");
    }
}
