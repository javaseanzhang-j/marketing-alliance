package com.aiaffiliate.notion.schema;

import java.util.List;

/** 声明式基础字段定义。 */
public record NotionPropertySchema(String name, NotionPropertyType type, List<String> options) {
    public NotionPropertySchema { options = options == null ? List.of() : List.copyOf(options); }
}
