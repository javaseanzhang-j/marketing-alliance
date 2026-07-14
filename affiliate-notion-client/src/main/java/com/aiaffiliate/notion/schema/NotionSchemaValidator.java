package com.aiaffiliate.notion.schema;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/** 非破坏性 Schema 校验器：允许多余字段和合法的镜像 Relation 差异。 */
public class NotionSchemaValidator {
    public SchemaValidationReport validate(NotionDatabaseSchemaDefinition expected, JsonNode actual) {
        return validate(expected, actual, Map.of());
    }

    /** targetDatabaseIds 使用 Schema logicalName 作为键；为空时只校验 Relation 类型。 */
    public SchemaValidationReport validate(NotionDatabaseSchemaDefinition expected, JsonNode actual,
                                           Map<String, String> targetDatabaseIds) {
        JsonNode properties = actual.path("properties");
        List<String> missing = new ArrayList<>(), incompatible = new ArrayList<>(), missingOptions = new ArrayList<>(), invalidRelations = new ArrayList<>(), warnings = new ArrayList<>(), actions = new ArrayList<>();
        for (NotionPropertySchema property : expected.baseProperties()) {
            JsonNode remote = properties.path(property.name());
            if (remote.isMissingNode()) { missing.add(property.name()); actions.add("添加字段: " + property.name()); continue; }
            String remoteType = remote.path("type").asText();
            boolean statusFallback = property.type() == NotionPropertyType.STATUS && "select".equals(remoteType);
            if (!property.type().apiName().equals(remoteType) && !statusFallback) { incompatible.add(property.name() + " expected=" + property.type().apiName() + " actual=" + remoteType); continue; }
            if (statusFallback) warnings.add(property.name() + " 使用 Select 兼容 Status");
            if (!property.options().isEmpty() && ("select".equals(remoteType) || "multi_select".equals(remoteType) || "status".equals(remoteType))) {
                Set<String> remoteOptions = new HashSet<>(); remote.path(remoteType).path("options").forEach(option -> remoteOptions.add(option.path("name").asText()));
                property.options().stream().filter(option -> !remoteOptions.contains(option)).forEach(option -> missingOptions.add(property.name() + ":" + option));
            }
        }
        for (NotionRelationSchema relation : expected.relations()) {
            JsonNode remote = properties.path(relation.name());
            if (remote.isMissingNode()) { missing.add(relation.name()); actions.add("创建 Relation: " + relation.name()); }
            else if (!"relation".equals(remote.path("type").asText())) invalidRelations.add(relation.name() + " 不是 Relation 类型");
            else {
                String expectedTarget = normalizeId(targetDatabaseIds.get(relation.targetLogicalName()));
                String actualTarget = normalizeId(remote.path("relation").path("database_id").asText(null));
                if (expectedTarget != null && actualTarget != null && !expectedTarget.equals(actualTarget)) {
                    invalidRelations.add(relation.name() + " 指向错误 Database，expected=" + expectedTarget + " actual=" + actualTarget);
                    actions.add("人工修复 Relation 指向: " + relation.name());
                }
            }
        }
        boolean valid = missing.isEmpty() && incompatible.isEmpty() && missingOptions.isEmpty() && invalidRelations.isEmpty();
        if (!incompatible.isEmpty()) actions.add("人工处理类型冲突；初始化器不会覆盖字段");
        return new SchemaValidationReport(expected.displayName(), valid, missing, incompatible, missingOptions, invalidRelations, warnings, actions);
    }

    private static String normalizeId(String id) {
        if (id == null || id.isBlank()) return null;
        return id.replace("-", "").trim().toLowerCase(Locale.ROOT);
    }
}
