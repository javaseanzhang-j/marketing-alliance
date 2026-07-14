package com.aiaffiliate.notion.schema;

/** 支持的 Notion Property 类型。 */
public enum NotionPropertyType {
    TITLE("title"), RICH_TEXT("rich_text"), SELECT("select"), MULTI_SELECT("multi_select"), NUMBER("number"),
    URL("url"), DATE("date"), FILES("files"), STATUS("status"), CREATED_TIME("created_time"),
    LAST_EDITED_TIME("last_edited_time"), RELATION("relation"), ROLLUP("rollup"), FORMULA("formula");
    private final String apiName; NotionPropertyType(String apiName) { this.apiName = apiName; } public String apiName() { return apiName; }
}
