package com.aiaffiliate.notion.schema;

import java.util.List;

/** 单个 Notion Database 的完整声明。 */
public interface NotionDatabaseSchemaDefinition {
    String logicalName(); String displayName(); List<NotionPropertySchema> baseProperties();
    List<NotionRelationSchema> relations(); List<NotionRollupSchema> rollups(); List<NotionFormulaSchema> formulas();
}
