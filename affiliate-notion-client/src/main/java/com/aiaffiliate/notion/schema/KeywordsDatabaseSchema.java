package com.aiaffiliate.notion.schema;

import java.util.List;
import static com.aiaffiliate.notion.schema.NotionPropertyType.*;
import static com.aiaffiliate.notion.schema.SchemaSupport.*;

/** Keywords Database Schema。 */
public class KeywordsDatabaseSchema implements NotionDatabaseSchemaDefinition {
    public String logicalName() { return "keywords"; } public String displayName() { return "Keywords"; }
    public List<NotionPropertySchema> baseProperties() { return managed(List.of(
            p("Keyword", TITLE), p("Keyword ID", RICH_TEXT), p("Category", SELECT), p("Subcategory", SELECT),
            p("Intent", SELECT, "INFORMATIONAL","COMMERCIAL","TRANSACTIONAL","COMPARISON","PROBLEM_SOLVING","INSPIRATIONAL"),
            p("Source", MULTI_SELECT, "MANUAL","FIVERR","PINTEREST","GOOGLE","REDDIT","AI_GENERATED","COMPETITOR","TREND_DISCOVERY"),
            p("Language", SELECT), p("Country", MULTI_SELECT), p("Search Volume", NUMBER), p("Pinterest Demand", NUMBER),
            p("Commercial Intent", NUMBER), p("Competition Score", NUMBER), p("Trend Score", NUMBER), p("Keyword Score", NUMBER),
            p("Status", STATUS, "INBOX","TO_RESEARCH","RESEARCHING","QUALIFIED","REJECTED","ARCHIVED"), p("Notes", RICH_TEXT),
            p("Created Time", CREATED_TIME), p("Last Edited Time", LAST_EDITED_TIME))); }
    public List<NotionRelationSchema> relations() { return List.of(r("Products","affiliate-products","Keywords"), r("Opportunities","opportunities","Primary Keyword"), r("Bridge Pages","bridge-pages","Primary Keyword"), r("Pinterest Content","pinterest-content","Primary Keyword")); }
    public List<NotionRollupSchema> rollups() { return List.of(); } public List<NotionFormulaSchema> formulas() { return List.of(); }
}
