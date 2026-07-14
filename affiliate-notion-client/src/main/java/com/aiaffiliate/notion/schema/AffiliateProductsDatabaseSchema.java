package com.aiaffiliate.notion.schema;

import java.util.List;
import static com.aiaffiliate.notion.schema.NotionPropertyType.*;
import static com.aiaffiliate.notion.schema.SchemaSupport.*;

/** Affiliate Products Database Schema。 */
public class AffiliateProductsDatabaseSchema implements NotionDatabaseSchemaDefinition {
    public String logicalName() { return "affiliate-products"; } public String displayName() { return "Affiliate Products"; }
    public List<NotionPropertySchema> baseProperties() { return managed(List.of(
            p("Product Name", TITLE), p("Product ID", RICH_TEXT), p("Platform", SELECT,"FIVERR"),
            p("Product Type", SELECT,"SERVICE_CATEGORY","SPECIFIC_GIG","SELLER_PROFILE","AFFILIATE_OFFER","BUNDLE"),
            p("Category", SELECT), p("Subcategory", SELECT), p("Fiverr Keyword", RICH_TEXT), p("Product URL", URL), p("Affiliate URL", URL),
            p("Seller Name", RICH_TEXT), p("Seller Level", SELECT,"NEW_SELLER","LEVEL_1","LEVEL_2","TOP_RATED","PRO","UNKNOWN"),
            p("Price Min", NUMBER), p("Price Max", NUMBER), p("Estimated AOV", NUMBER), p("Rating", NUMBER), p("Review Count", NUMBER),
            p("Delivery Days", NUMBER), p("Orders in Queue", NUMBER), p("Target Audience", MULTI_SELECT), p("Primary Pain Point", RICH_TEXT),
            p("Visual Potential", NUMBER), p("Purchase Intent", NUMBER), p("Commission Potential", NUMBER), p("Product Quality Score", NUMBER),
            p("Status", STATUS,"DISCOVERED","RESEARCHING","QUALIFIED","SELECTED","TESTING","ACTIVE","PAUSED","REJECTED","ARCHIVED"),
            p("Data Source", MULTI_SELECT), p("Last Checked Date", DATE), p("Notes", RICH_TEXT), p("Created Time", CREATED_TIME), p("Last Edited Time", LAST_EDITED_TIME))); }
    public List<NotionRelationSchema> relations() { return List.of(r("Keywords","keywords","Products"), r("Opportunities","opportunities","Product"), r("Bridge Pages","bridge-pages","Products"), r("Pinterest Content","pinterest-content","Product")); }
    public List<NotionRollupSchema> rollups() { return List.of(new NotionRollupSchema("Opportunity Count","Opportunities","Opportunity Name","count_all",false), new NotionRollupSchema("Bridge Page Count","Bridge Pages","Page Name","count_all",false)); }
    public List<NotionFormulaSchema> formulas() { return List.of(); }
}
