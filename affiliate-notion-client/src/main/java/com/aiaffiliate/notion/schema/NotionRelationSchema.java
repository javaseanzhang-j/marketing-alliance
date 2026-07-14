package com.aiaffiliate.notion.schema;

/** 声明式双向或单向 Relation 定义。 */
public record NotionRelationSchema(String name, String targetLogicalName, boolean dualProperty, String mirroredPropertyName) {}
