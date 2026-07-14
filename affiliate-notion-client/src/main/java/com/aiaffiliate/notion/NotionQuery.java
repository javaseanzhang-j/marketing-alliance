package com.aiaffiliate.notion;

import java.util.List;
import java.util.Map;

/** Notion Database 查询条件；filter 与 sorts 直接对应官方 API 的 JSON 结构。 */
public record NotionQuery(
        Map<String, Object> filter,
        List<Map<String, Object>> sorts,
        String startCursor,
        Integer pageSize) {

    public NotionQuery {
        filter = filter == null ? Map.of() : Map.copyOf(filter);
        sorts = sorts == null ? List.of() : List.copyOf(sorts);
        if (pageSize != null && (pageSize < 1 || pageSize > 100)) {
            throw new IllegalArgumentException("pageSize must be between 1 and 100");
        }
    }

    public static NotionQuery firstPage(int pageSize) {
        return new NotionQuery(Map.of(), List.of(), null, pageSize);
    }
}

