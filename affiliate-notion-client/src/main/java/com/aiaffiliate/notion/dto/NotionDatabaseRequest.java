package com.aiaffiliate.notion.dto;

import java.util.List;
import java.util.Map;

/** Notion Database 创建请求。 */
public record NotionDatabaseRequest(Map<String, Object> parent, List<Map<String, Object>> title, Map<String, Object> properties) {
    public NotionDatabaseRequest { parent = Map.copyOf(parent); title = List.copyOf(title); properties = Map.copyOf(properties); }
    public static NotionDatabaseRequest underPage(String pageId, String title, Map<String, Object> properties) {
        return new NotionDatabaseRequest(Map.of("type", "page_id", "page_id", pageId),
                List.of(Map.of("type", "text", "text", Map.of("content", title))), properties);
    }
}
