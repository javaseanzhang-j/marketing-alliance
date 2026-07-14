package com.aiaffiliate.application.notion;

import java.util.List;

/** 对外暴露的 Notion Schema 校验状态。 */
public record SchemaStatus(String databaseName, boolean valid, List<String> missingProperties,
                           List<String> incompatibleProperties, List<String> missingSelectOptions,
                           List<String> invalidRelations, List<String> warnings, List<String> recommendedActions) {
    public SchemaStatus {
        missingProperties = List.copyOf(missingProperties); incompatibleProperties = List.copyOf(incompatibleProperties);
        missingSelectOptions = List.copyOf(missingSelectOptions); invalidRelations = List.copyOf(invalidRelations);
        warnings = List.copyOf(warnings); recommendedActions = List.copyOf(recommendedActions);
    }
}
