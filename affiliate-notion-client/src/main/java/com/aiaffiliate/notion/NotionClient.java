package com.aiaffiliate.notion;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * Notion 数据访问边界。调用方传入 Notion properties JSON，避免 SDK 类型污染领域层。
 */
public interface NotionClient {

    JsonNode queryDatabase(String databaseId, NotionQuery query);

    JsonNode createPage(String databaseId, Map<String, Object> properties);

    JsonNode updatePage(String pageId, Map<String, Object> properties);
}

