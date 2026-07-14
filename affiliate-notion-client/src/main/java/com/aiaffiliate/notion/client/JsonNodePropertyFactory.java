package com.aiaffiliate.notion.client;

import java.util.Map;

/** Relation、Rollup 和 Formula Schema 字段请求工厂。 */
public class JsonNodePropertyFactory {
    public Map<String, Object> relation(String databaseId, boolean dualProperty) {
        return Map.of("relation", Map.of("database_id", databaseId, "type", dualProperty ? "dual_property" : "single_property",
                dualProperty ? "dual_property" : "single_property", Map.of()));
    }
    public Map<String, Object> rollup(String relationProperty, String rollupProperty, String function) {
        return Map.of("rollup", Map.of("relation_property_name", relationProperty, "rollup_property_name", rollupProperty, "function", function));
    }
    public Map<String, Object> formula(String expression) { return Map.of("formula", Map.of("expression", expression)); }
}
