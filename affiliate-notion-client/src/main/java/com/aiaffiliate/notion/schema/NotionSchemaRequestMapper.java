package com.aiaffiliate.notion.schema;

import java.util.*;

/** 将声明式 Schema 转换为 Notion API property 请求。 */
public class NotionSchemaRequestMapper {
    public Map<String, Object> baseProperties(NotionDatabaseSchemaDefinition schema, boolean statusAsSelect) {
        Map<String, Object> result = new LinkedHashMap<>();
        schema.baseProperties().forEach(property -> result.put(property.name(), property(property, statusAsSelect)));
        return result;
    }
    public Map<String, Object> property(NotionPropertySchema property, boolean statusAsSelect) {
        NotionPropertyType type = statusAsSelect && property.type() == NotionPropertyType.STATUS ? NotionPropertyType.SELECT : property.type();
        Map<String, Object> body = new LinkedHashMap<>();
        if (type == NotionPropertyType.SELECT || type == NotionPropertyType.MULTI_SELECT || type == NotionPropertyType.STATUS) {
            List<Map<String, String>> options = property.options().stream().map(value -> Map.of("name", value)).toList();
            body.put(type.apiName(), Map.of("options", options));
        } else if (type == NotionPropertyType.NUMBER) body.put("number", Map.of("format", "number"));
        else body.put(type.apiName(), Map.of());
        return body;
    }
    public Map<String, Object> relation(NotionRelationSchema relation, String targetDatabaseId) {
        Map<String, Object> relationBody = new LinkedHashMap<>(); relationBody.put("database_id", targetDatabaseId);
        relationBody.put("type", relation.dualProperty() ? "dual_property" : "single_property");
        if (relation.dualProperty()) relationBody.put("dual_property", Map.of("synced_property_name", relation.mirroredPropertyName()));
        else relationBody.put("single_property", Map.of());
        return Map.of("relation", relationBody);
    }
    public Map<String, Object> rollup(NotionRollupSchema rollup) { return Map.of("rollup", Map.of("relation_property_name", rollup.relationProperty(), "rollup_property_name", rollup.targetProperty(), "function", rollup.function())); }
    public Map<String, Object> formula(NotionFormulaSchema formula) { return Map.of("formula", Map.of("expression", formula.expression())); }
}
