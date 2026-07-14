package com.aiaffiliate.notion.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/** 类型安全的 Notion 分页结果。 */
public record NotionPageResult(List<JsonNode> results, boolean hasMore, String nextCursor) {
    public NotionPageResult { results = List.copyOf(results); }
}
