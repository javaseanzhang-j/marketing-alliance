package com.aiaffiliate.application.notion;

import java.util.Map;

/** Notion 初始化执行结果。 */
public record InitializationResult(boolean success, Map<String, String> databaseIds, java.util.List<SchemaStatus> schemas) {
    public InitializationResult { databaseIds = Map.copyOf(databaseIds); schemas = java.util.List.copyOf(schemas); }
}
