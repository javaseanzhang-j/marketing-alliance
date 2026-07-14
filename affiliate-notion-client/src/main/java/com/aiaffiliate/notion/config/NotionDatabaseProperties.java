package com.aiaffiliate.notion.config;

/** 七个逻辑数据库的可选远程 ID。 */
public record NotionDatabaseProperties(String keywords, String affiliateProducts, String opportunities,
                                       String bridgePages, String pinterestContent,
                                       String productSnapshots, String campaignMetrics) {
    public String byLogicalName(String name) {
        return switch (name) {
            case "keywords" -> keywords; case "affiliate-products" -> affiliateProducts;
            case "opportunities" -> opportunities; case "bridge-pages" -> bridgePages;
            case "pinterest-content" -> pinterestContent; case "product-snapshots" -> productSnapshots;
            case "campaign-metrics" -> campaignMetrics; default -> null;
        };
    }
}
