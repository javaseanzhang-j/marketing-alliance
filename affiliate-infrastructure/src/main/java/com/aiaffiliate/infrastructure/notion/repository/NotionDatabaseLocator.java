package com.aiaffiliate.infrastructure.notion.repository;

import com.aiaffiliate.notion.config.NotionProperties;
import com.aiaffiliate.notion.registry.DatabaseIdRegistry;

/** 从配置或初始化 Registry 解析逻辑 Database ID。 */
public class NotionDatabaseLocator {
    private final NotionProperties properties; private final DatabaseIdRegistry registry;
    public NotionDatabaseLocator(NotionProperties properties, DatabaseIdRegistry registry){this.properties=properties;this.registry=registry;}
    public String require(String logicalName){String configured=properties.databases().byLogicalName(logicalName);if(configured!=null&&!configured.isBlank())return configured;String saved=registry.load().get(logicalName);if(saved==null||saved.isBlank())throw new IllegalStateException("Notion database is not initialized: "+logicalName);return saved;}
}
