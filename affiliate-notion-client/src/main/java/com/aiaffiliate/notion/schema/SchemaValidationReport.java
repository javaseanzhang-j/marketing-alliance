package com.aiaffiliate.notion.schema;

import java.util.List;

/** 远程 Schema 与声明定义的差异报告。 */
public record SchemaValidationReport(String databaseName, boolean valid, List<String> missingProperties,
                                     List<String> incompatibleProperties, List<String> missingSelectOptions,
                                     List<String> invalidRelations, List<String> warnings, List<String> recommendedActions) {
    public SchemaValidationReport {
        missingProperties = List.copyOf(missingProperties); incompatibleProperties = List.copyOf(incompatibleProperties);
        missingSelectOptions = List.copyOf(missingSelectOptions); invalidRelations = List.copyOf(invalidRelations);
        warnings = List.copyOf(warnings); recommendedActions = List.copyOf(recommendedActions);
    }
}
