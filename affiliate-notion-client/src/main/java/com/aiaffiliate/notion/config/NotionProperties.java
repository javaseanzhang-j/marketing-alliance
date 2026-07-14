package com.aiaffiliate.notion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

/** Notion API、超时、重试及数据库 ID 配置。 */
@ConfigurationProperties(prefix = "notion")
public record NotionProperties(boolean enabled, String baseUrl, String token, String apiVersion,
                               String parentPageId, Duration connectTimeout, Duration readTimeout,
                               int maxRetries, Initialization initialization, NotionDatabaseProperties databases) {
    public NotionProperties {
        baseUrl = baseUrl == null || baseUrl.isBlank() ? "https://api.notion.com" : baseUrl;
        apiVersion = apiVersion == null || apiVersion.isBlank() ? "2022-06-28" : apiVersion;
        connectTimeout = connectTimeout == null ? Duration.ofSeconds(5) : connectTimeout;
        readTimeout = readTimeout == null ? Duration.ofSeconds(20) : readTimeout;
        if (maxRetries < 0) throw new IllegalArgumentException("notion.max-retries must not be negative");
        initialization = initialization == null ? new Initialization(false, ".notion-databases.json") : initialization;
        databases = databases == null ? new NotionDatabaseProperties(null, null, null, null, null, null, null) : databases;
    }
    public record Initialization(boolean enabled, String registryPath) {
        public Initialization { registryPath = registryPath == null || registryPath.isBlank() ? ".notion-databases.json" : registryPath; }
    }
}
