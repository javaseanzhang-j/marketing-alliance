package com.aiaffiliate.notion.client;

import com.aiaffiliate.notion.dto.NotionPageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

/** Notion Page 和 Block 操作。 */
public interface NotionPageClient {
    JsonNode createPage(NotionPageRequest request);
    JsonNode getPage(String pageId);
    JsonNode updatePageProperties(String pageId, Map<String, Object> properties);
    JsonNode archivePage(String pageId);
    JsonNode appendPageBlocks(String pageId, List<Map<String, Object>> children);
}
