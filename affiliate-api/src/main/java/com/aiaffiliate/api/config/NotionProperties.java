package com.aiaffiliate.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Notion Token 与五个业务 Database ID 的外部化配置。 */
@ConfigurationProperties(prefix = "affiliate.notion")
public record NotionProperties(
        boolean enabled,
        String token,
        int timeoutSeconds,
        Databases databases) {

    public NotionProperties {
        timeoutSeconds = timeoutSeconds <= 0 ? 10 : timeoutSeconds;
        databases = databases == null ? new Databases(null, null, null, null, null) : databases;
    }

    public record Databases(
            String products,
            String keywords,
            String opportunities,
            String bridgePages,
            String pinterestContent) {
    }
}

