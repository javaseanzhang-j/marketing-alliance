package com.aiaffiliate.notion.client;

import com.aiaffiliate.notion.dto.NotionDatabaseRequest;
import com.aiaffiliate.notion.dto.NotionPageResult;
import com.aiaffiliate.notion.dto.NotionQueryRequest;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

/** Notion Database/Data Source 操作。 */
public interface NotionDatabaseClient {
    JsonNode createDatabase(NotionDatabaseRequest request);
    JsonNode getDatabaseSchema(String databaseId);
    JsonNode updateDatabaseSchema(String databaseId, Map<String, Object> properties);
    NotionPageResult queryDatabase(String databaseId, NotionQueryRequest request);
    default NotionPageResult getNextPage(String databaseId, NotionQueryRequest previous, String nextCursor) {
        return queryDatabase(databaseId, new NotionQueryRequest(previous.filter(), previous.sorts(), nextCursor, previous.pageSize()));
    }
}
