package com.aiaffiliate.notion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/** Notion 数据源分页查询请求。 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record NotionQueryRequest(Map<String, Object> filter, List<Map<String, Object>> sorts,
                                 @JsonProperty("start_cursor") String startCursor,
                                 @JsonProperty("page_size") Integer pageSize) {
    public NotionQueryRequest {
        filter = filter == null ? Map.of() : Map.copyOf(filter); sorts = sorts == null ? List.of() : List.copyOf(sorts);
        if (pageSize != null && (pageSize < 1 || pageSize > 100)) throw new IllegalArgumentException("pageSize must be between 1 and 100");
    }
    public static NotionQueryRequest firstPage(int size) { return new NotionQueryRequest(Map.of(), List.of(), null, size); }
}
