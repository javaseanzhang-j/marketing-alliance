package com.aiaffiliate.notion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

/** Notion Page 创建请求。 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record NotionPageRequest(Map<String, Object> parent, Map<String, Object> properties, List<Map<String, Object>> children) {
    public NotionPageRequest { parent = Map.copyOf(parent); properties = Map.copyOf(properties); children = children == null ? List.of() : List.copyOf(children); }
    public static NotionPageRequest inDatabase(String databaseId, Map<String, Object> properties, List<Map<String, Object>> children) {
        return new NotionPageRequest(Map.of("database_id", databaseId), properties, children);
    }
}
