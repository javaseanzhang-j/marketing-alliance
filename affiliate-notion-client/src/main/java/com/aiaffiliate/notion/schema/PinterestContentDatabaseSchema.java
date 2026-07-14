package com.aiaffiliate.notion.schema;

import java.util.List;
import static com.aiaffiliate.notion.schema.NotionPropertyType.*;
import static com.aiaffiliate.notion.schema.SchemaSupport.*;

/** Pinterest Content Database Schema。 */
public class PinterestContentDatabaseSchema implements NotionDatabaseSchemaDefinition {
    public String logicalName() { return "pinterest-content"; } public String displayName() { return "Pinterest Content"; }
    public List<NotionPropertySchema> baseProperties() { return managed(List.of(
            p("Pin Name", TITLE), p("Pin ID", RICH_TEXT), p("Pin Type", SELECT), p("Creative Angle", SELECT), p("Pin Title", RICH_TEXT),
            p("Pin Description", RICH_TEXT), p("Overlay Text", RICH_TEXT), p("Image Prompt", RICH_TEXT), p("Design Template", SELECT), p("Asset URL", URL),
            p("Destination URL", URL), p("UTM Source", RICH_TEXT), p("UTM Medium", RICH_TEXT), p("UTM Campaign", RICH_TEXT), p("UTM Content", RICH_TEXT),
            p("Final URL", URL), p("Board", SELECT), p("Publishing Type", SELECT,"ORGANIC","PAID","RETARGETING"), p("Publish Date", DATE),
            p("Published URL", URL), p("Status", STATUS,"IDEA","DRAFT","DESIGNING","REVIEW","APPROVED","SCHEDULED","PUBLISHED","TESTING","PAUSED","ARCHIVED"),
            p("Compliance Status", SELECT,"NOT_REVIEWED","NEEDS_REVISION","APPROVED","REJECTED"), p("Created Time", CREATED_TIME), p("Last Edited Time", LAST_EDITED_TIME))); }
    public List<NotionRelationSchema> relations() { return List.of(r("Opportunity","opportunities","Pinterest Content"), r("Bridge Page","bridge-pages","Pinterest Content"), r("Product","affiliate-products","Pinterest Content"), r("Primary Keyword","keywords","Pinterest Content")); }
    public List<NotionRollupSchema> rollups() { return List.of(); } public List<NotionFormulaSchema> formulas() { return List.of(); }
}
