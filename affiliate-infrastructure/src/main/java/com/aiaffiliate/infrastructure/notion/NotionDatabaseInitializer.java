package com.aiaffiliate.infrastructure.notion;

import com.aiaffiliate.application.notion.*;
import com.aiaffiliate.notion.client.NotionClient;
import com.aiaffiliate.notion.config.NotionProperties;
import com.aiaffiliate.notion.dto.NotionDatabaseRequest;
import com.aiaffiliate.notion.exception.NotionResourceNotFoundException;
import com.aiaffiliate.notion.exception.NotionValidationException;
import com.aiaffiliate.notion.registry.DatabaseIdRegistry;
import com.aiaffiliate.notion.schema.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/**
 * 幂等 Notion Database 初始化器。先创建基础字段，再创建 Relation，最后处理启用的 Rollup/Formula。
 */
public class NotionDatabaseInitializer implements NotionAdministrationUseCase {
    private final NotionClient client; private final NotionProperties properties; private final DatabaseIdRegistry registry;
    private final List<NotionDatabaseSchemaDefinition> schemas; private final NotionSchemaValidator validator;
    private final NotionSchemaRequestMapper requestMapper;

    public NotionDatabaseInitializer(NotionClient client, NotionProperties properties, DatabaseIdRegistry registry,
                                     List<NotionDatabaseSchemaDefinition> schemas, NotionSchemaValidator validator,
                                     NotionSchemaRequestMapper requestMapper) {
        this.client = client; this.properties = properties; this.registry = registry; this.schemas = List.copyOf(schemas);
        this.validator = validator; this.requestMapper = requestMapper;
    }

    @Override public synchronized InitializationResult initialize() {
        validateConfiguration();
        Map<String, String> ids = mergedIds();
        try {
            for (NotionDatabaseSchemaDefinition schema : schemas) ensureBaseDatabase(schema, ids);
            registry.save(ids);
            for (NotionDatabaseSchemaDefinition schema : schemas) ensureRelations(schema, ids);
            for (NotionDatabaseSchemaDefinition schema : schemas) ensureDerivedProperties(schema, ids);
            registry.save(ids);
            List<SchemaStatus> reports = validateSchemas(ids);
            return new InitializationResult(reports.stream().allMatch(SchemaStatus::valid), ids, reports);
        } catch (RuntimeException failure) {
            throw failure instanceof NotionInitializationException ? failure : new NotionInitializationException("Notion database initialization failed", failure);
        }
    }

    @Override public List<SchemaStatus> validateSchemas() { return validateSchemas(mergedIds()); }

    private List<SchemaStatus> validateSchemas(Map<String, String> ids) {
        List<SchemaStatus> result = new ArrayList<>();
        for (NotionDatabaseSchemaDefinition schema : schemas) {
            String id = ids.get(schema.logicalName());
            if (id == null || id.isBlank()) {
                result.add(new SchemaStatus(schema.displayName(), false, List.of("Database ID"), List.of(), List.of(), List.of(), List.of("数据库尚未初始化"), List.of("执行 Notion 初始化")));
                continue;
            }
            try { result.add(toStatus(validator.validate(schema, client.getDatabaseSchema(id), ids))); }
            catch (NotionResourceNotFoundException e) { result.add(new SchemaStatus(schema.displayName(), false, List.of(), List.of(), List.of(), List.of(), List.of("远程 Database 不存在或无权限"), List.of("检查 Database ID 和 Integration 权限"))); }
        }
        return result;
    }

    private void ensureBaseDatabase(NotionDatabaseSchemaDefinition schema, Map<String, String> ids) {
        String existingId = ids.get(schema.logicalName());
        if (existingId != null && !existingId.isBlank()) {
            JsonNode remote = client.getDatabaseSchema(existingId);
            SchemaValidationReport report = validator.validate(schema, remote);
            if (!report.incompatibleProperties().isEmpty()) throw new NotionSchemaMismatchException(schema.displayName() + " has incompatible properties: " + report.incompatibleProperties());
            addMissingBaseProperties(schema, remote, report);
            addMissingSelectOptions(schema, remote, report);
            return;
        }
        NotionDatabaseRequest request = NotionDatabaseRequest.underPage(properties.parentPageId(), schema.displayName(), requestMapper.baseProperties(schema, false));
        JsonNode created;
        try { created = client.createDatabase(request); }
        catch (NotionValidationException statusUnsupported) {
            created = client.createDatabase(NotionDatabaseRequest.underPage(properties.parentPageId(), schema.displayName(), requestMapper.baseProperties(schema, true)));
        }
        String id = created.path("id").asText();
        if (id.isBlank()) throw new NotionInitializationException("Notion did not return an id for " + schema.displayName());
        ids.put(schema.logicalName(), id);
    }

    private void addMissingBaseProperties(NotionDatabaseSchemaDefinition schema, JsonNode remote, SchemaValidationReport report) {
        Map<String, Object> additions = new LinkedHashMap<>();
        Set<String> relationNames = new HashSet<>(); schema.relations().forEach(value -> relationNames.add(value.name()));
        for (String missing : report.missingProperties()) {
            if (relationNames.contains(missing)) continue;
            schema.baseProperties().stream().filter(property -> property.name().equals(missing)).findFirst()
                    .ifPresent(property -> additions.put(missing, requestMapper.property(property, false)));
        }
        if (!additions.isEmpty()) {
            try { client.updateDatabaseSchema(remote.path("id").asText(), additions); }
            catch (NotionValidationException statusUnsupported) {
                for (NotionPropertySchema property : schema.baseProperties()) {
                    if (additions.containsKey(property.name())) additions.put(property.name(), requestMapper.property(property, true));
                }
                client.updateDatabaseSchema(remote.path("id").asText(), additions);
            }
        }
    }

    /** 补充声明中缺少的枚举值，同时保留用户在 Notion 中额外添加的选项。 */
    private void addMissingSelectOptions(NotionDatabaseSchemaDefinition schema, JsonNode remote, SchemaValidationReport report) {
        if (report.missingSelectOptions().isEmpty()) return;
        Map<String, Object> updates = new LinkedHashMap<>();
        JsonNode remoteProperties = remote.path("properties");
        for (NotionPropertySchema property : schema.baseProperties()) {
            if (property.options().isEmpty()) continue;
            JsonNode remoteProperty = remoteProperties.path(property.name());
            String remoteType = remoteProperty.path("type").asText();
            if (!Set.of("select", "multi_select", "status").contains(remoteType)) continue;
            LinkedHashSet<String> options = new LinkedHashSet<>();
            remoteProperty.path(remoteType).path("options").forEach(option -> options.add(option.path("name").asText()));
            boolean changed = options.addAll(property.options());
            if (changed) {
                List<Map<String, String>> values = options.stream().filter(value -> !value.isBlank()).map(value -> Map.of("name", value)).toList();
                updates.put(property.name(), Map.of(remoteType, Map.of("options", values)));
            }
        }
        if (!updates.isEmpty()) client.updateDatabaseSchema(remote.path("id").asText(), updates);
    }

    private void ensureRelations(NotionDatabaseSchemaDefinition schema, Map<String, String> ids) {
        String databaseId = ids.get(schema.logicalName()); JsonNode remote = client.getDatabaseSchema(databaseId);
        for (NotionRelationSchema relation : schema.relations()) {
            if (remote.path("properties").has(relation.name())) continue;
            String target = ids.get(relation.targetLogicalName());
            if (target == null) throw new NotionInitializationException("Missing target database for relation " + relation.name());
            client.updateDatabaseSchema(databaseId, Map.of(relation.name(), requestMapper.relation(relation, target)));
            remote = client.getDatabaseSchema(databaseId);
        }
    }

    private void ensureDerivedProperties(NotionDatabaseSchemaDefinition schema, Map<String, String> ids) {
        String databaseId = ids.get(schema.logicalName()); JsonNode remote = client.getDatabaseSchema(databaseId);
        Map<String, Object> additions = new LinkedHashMap<>();
        schema.rollups().stream().filter(NotionRollupSchema::enabled).filter(value -> !remote.path("properties").has(value.name())).forEach(value -> additions.put(value.name(), requestMapper.rollup(value)));
        schema.formulas().stream().filter(NotionFormulaSchema::enabled).filter(value -> !remote.path("properties").has(value.name())).forEach(value -> additions.put(value.name(), requestMapper.formula(value)));
        if (!additions.isEmpty()) client.updateDatabaseSchema(databaseId, additions);
    }

    private Map<String, String> mergedIds() {
        Map<String, String> result = new LinkedHashMap<>(registry.load());
        for (NotionDatabaseSchemaDefinition schema : schemas) {
            String configured = properties.databases().byLogicalName(schema.logicalName());
            if (configured != null && !configured.isBlank()) result.put(schema.logicalName(), configured.trim());
        }
        return result;
    }
    private void validateConfiguration() {
        if (properties.token() == null || properties.token().isBlank()) throw new NotionInitializationException("NOTION_TOKEN is required");
        if (properties.parentPageId() == null || properties.parentPageId().isBlank()) throw new NotionInitializationException("NOTION_PARENT_PAGE_ID is required");
    }
    private SchemaStatus toStatus(SchemaValidationReport report) { return new SchemaStatus(report.databaseName(), report.valid(), report.missingProperties(), report.incompatibleProperties(), report.missingSelectOptions(), report.invalidRelations(), report.warnings(), report.recommendedActions()); }
}
