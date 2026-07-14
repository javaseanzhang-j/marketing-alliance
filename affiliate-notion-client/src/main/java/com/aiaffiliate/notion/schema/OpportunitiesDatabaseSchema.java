package com.aiaffiliate.notion.schema;

import java.util.List;
import static com.aiaffiliate.notion.schema.NotionPropertyType.*;
import static com.aiaffiliate.notion.schema.SchemaSupport.*;

/** Opportunities Database Schema。 */
public class OpportunitiesDatabaseSchema implements NotionDatabaseSchemaDefinition {
    public String logicalName() { return "opportunities"; } public String displayName() { return "Opportunities"; }
    public List<NotionPropertySchema> baseProperties() { return managed(List.of(
            p("Opportunity Name", TITLE), p("Opportunity ID", RICH_TEXT), p("Channel", SELECT), p("Audience", MULTI_SELECT), p("Country", MULTI_SELECT),
            p("Funnel Stage", SELECT), p("Content Angle", SELECT), p("Fiverr Value Score", NUMBER), p("Pinterest Demand Score", NUMBER),
            p("SEO Opportunity Score", NUMBER), p("Commercial Intent Score", NUMBER), p("Competition Score", NUMBER), p("Content Potential Score", NUMBER),
            p("Confidence Score", NUMBER), p("Final Score", NUMBER), p("Priority", SELECT,"P0_IMMEDIATE_TEST","P1_HIGH","P2_MEDIUM","P3_LOW","REJECT"),
            p("Recommendation", SELECT), p("AI Summary", RICH_TEXT), p("Key Evidence", RICH_TEXT), p("Risks", RICH_TEXT), p("Test Budget", NUMBER),
            p("Status", STATUS,"DRAFT","ANALYZING","REVIEWED","APPROVED","TESTING","VALIDATED","SCALING","FAILED","ARCHIVED"),
            p("Created Time", CREATED_TIME), p("Last Edited Time", LAST_EDITED_TIME))); }
    public List<NotionRelationSchema> relations() { return List.of(r("Product","affiliate-products","Opportunities"), r("Primary Keyword","keywords","Opportunities"), new NotionRelationSchema("Secondary Keywords","keywords",false,null), r("Bridge Pages","bridge-pages","Opportunity"), r("Pinterest Content","pinterest-content","Opportunity")); }
    public List<NotionRollupSchema> rollups() { return List.of(new NotionRollupSchema("Bridge Page Count","Bridge Pages","Page Name","count_all",false), new NotionRollupSchema("Pin Count","Pinterest Content","Pin Name","count_all",false)); }
    public List<NotionFormulaSchema> formulas() { return List.of(); }
}
