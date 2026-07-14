package com.aiaffiliate.notion.schema;

import java.util.Arrays;
import java.util.List;

/** Schema Definition 的紧凑构建辅助类。 */
public final class SchemaSupport {
    private SchemaSupport() {}
    public static NotionPropertySchema p(String name, NotionPropertyType type, String... options) { return new NotionPropertySchema(name, type, Arrays.asList(options)); }
    public static NotionRelationSchema r(String name, String target, String mirror) { return new NotionRelationSchema(name, target, true, mirror); }
    public static List<NotionPropertySchema> managed(List<NotionPropertySchema> properties) {
        var result = new java.util.ArrayList<>(properties);
        result.add(p("Schema Version", NotionPropertyType.RICH_TEXT)); result.add(p("Managed By", NotionPropertyType.RICH_TEXT));
        return List.copyOf(result);
    }
}
