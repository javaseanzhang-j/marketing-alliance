package com.aiaffiliate.infrastructure.notion.mapper;

import com.aiaffiliate.application.reference.EntityType;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

/** 领域实体与 Notion Page 的双向映射契约。 */
public interface NotionEntityMapper<T> {
    String domainId(T entity); EntityType entityType();
    Map<String, Object> toCreateProperties(T entity); Map<String, Object> toUpdateProperties(T entity);
    default List<Map<String, Object>> toPageBody(T entity) { return List.of(); }
    T fromPage(JsonNode page);
}
