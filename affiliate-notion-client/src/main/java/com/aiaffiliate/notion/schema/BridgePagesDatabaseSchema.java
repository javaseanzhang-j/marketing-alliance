package com.aiaffiliate.notion.schema;

import java.util.List;
import static com.aiaffiliate.notion.schema.NotionPropertyType.*;
import static com.aiaffiliate.notion.schema.SchemaSupport.*;

/** Bridge Pages Database Schema。 */
public class BridgePagesDatabaseSchema implements NotionDatabaseSchemaDefinition {
    public String logicalName() { return "bridge-pages"; } public String displayName() { return "Bridge Pages"; }
    public List<NotionPropertySchema> baseProperties() { return managed(List.of(
            p("Page Name", TITLE), p("Bridge Page ID", RICH_TEXT), p("Page Type", SELECT), p("Audience", MULTI_SELECT), p("Channel", SELECT), p("Language", SELECT),
            p("URL Slug", RICH_TEXT), p("Published URL", URL), p("Headline", RICH_TEXT), p("Subheadline", RICH_TEXT), p("Meta Title", RICH_TEXT),
            p("Meta Description", RICH_TEXT), p("Hero CTA", RICH_TEXT), p("Primary CTA", RICH_TEXT), p("Secondary CTA", RICH_TEXT),
            p("Page Outline", RICH_TEXT), p("Full Content", RICH_TEXT), p("Disclosure Text", RICH_TEXT), p("Trust Elements", MULTI_SELECT),
            p("AI Model", SELECT), p("Prompt Version", RICH_TEXT), p("Content Score", NUMBER), p("Compliance Status", SELECT),
            p("Status", STATUS,"IDEA","GENERATING","DRAFT","REVIEW","APPROVED","BUILDING","PUBLISHED","OPTIMIZING","PAUSED","ARCHIVED"),
            p("Published Date", DATE), p("Created Time", CREATED_TIME), p("Last Updated", LAST_EDITED_TIME))); }
    public List<NotionRelationSchema> relations() { return List.of(r("Opportunity","opportunities","Bridge Pages"), r("Products","affiliate-products","Bridge Pages"), r("Primary Keyword","keywords","Bridge Pages"), r("Pinterest Content","pinterest-content","Bridge Page")); }
    public List<NotionRollupSchema> rollups() { return List.of(new NotionRollupSchema("Pin Count","Pinterest Content","Pin Name","count_all",false)); }
    public List<NotionFormulaSchema> formulas() { return List.of(); }
}
