package com.aiaffiliate.domain.port;

import java.util.List;

/** 与存储技术无关的分页结果。 */
public record PageResult<T>(List<T> content, int page, int size, boolean hasNext, String nextCursor) {
    public PageResult { content = List.copyOf(content); }
}
