package com.aiaffiliate.notion.schema;

/** 预留的 Rollup 定义。 */
public record NotionRollupSchema(String name, String relationProperty, String targetProperty, String function, boolean enabled) {}
