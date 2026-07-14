package com.aiaffiliate.infrastructure.notion.mapper;

import com.aiaffiliate.application.reference.*;
import com.aiaffiliate.domain.model.*;
import com.aiaffiliate.notion.mapper.NotionPropertyMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/** AffiliateProduct 与 Notion Page 映射。 */
public class AffiliateProductNotionMapper extends AbstractNotionMapper<AffiliateProduct> {
    public AffiliateProductNotionMapper(NotionPropertyMapper p, EntityReferenceRepository refs) { super(p,refs); }
    public String domainId(AffiliateProduct v){return v.id().value();} public EntityType entityType(){return EntityType.AFFILIATE_PRODUCT;}
    public Map<String,Object> toCreateProperties(AffiliateProduct v){return properties(v);} public Map<String,Object> toUpdateProperties(AffiliateProduct v){return properties(v);}
    private Map<String,Object> properties(AffiliateProduct v){Map<String,Object> m=new LinkedHashMap<>();
        m.put("Product Name",p.title(v.productName()));m.put("Product ID",p.richText(v.id().value()));m.put("Platform",p.select(v.platform()));m.put("Product Type",p.select(v.productType()));
        m.put("Category",p.select(v.category()));m.put("Subcategory",p.select(v.subcategory()));m.put("Fiverr Keyword",p.richText(v.fiverrKeyword()));m.put("Product URL",p.url(v.productUrl()));m.put("Affiliate URL",p.url(v.affiliateUrl()));
        m.put("Seller Name",p.richText(v.sellerName()));m.put("Seller Level",p.select(v.sellerLevel()));m.put("Price Min",p.number(v.priceMin()));m.put("Price Max",p.number(v.priceMax()));m.put("Estimated AOV",p.number(v.estimatedAov()));
        m.put("Rating",p.number(v.rating()));m.put("Review Count",p.number(v.reviewCount()));m.put("Delivery Days",p.number(v.deliveryDays()));m.put("Orders in Queue",p.number(v.ordersInQueue()));
        m.put("Target Audience",p.multiSelect(v.targetAudiences()));m.put("Primary Pain Point",p.richText(v.primaryPainPoint()));m.put("Visual Potential",p.number(v.visualPotential()));m.put("Purchase Intent",p.number(v.purchaseIntent()));
        m.put("Commission Potential",p.number(v.commissionPotential()));m.put("Product Quality Score",p.number(v.productQualityScore()));m.put("Status",p.select(v.status()));m.put("Data Source",p.multiSelect(v.dataSources()));
        m.put("Last Checked Date",p.date(v.lastCheckedDate()));m.put("Notes",p.richText(v.notes()));return managed(m);}
    public AffiliateProduct fromPage(JsonNode n){return new AffiliateProduct(new ProductId(p.text(n,"Product ID")),p.text(n,"Product Name"),
            p.enumValue(p.select(n,"Platform"),AffiliatePlatform.class,AffiliatePlatform.UNKNOWN),p.enumValue(p.select(n,"Product Type"),ProductType.class,ProductType.UNKNOWN),
            value(p.select(n,"Category")),value(p.select(n,"Subcategory")),p.text(n,"Fiverr Keyword"),p.uri(n,"Product URL"),p.uri(n,"Affiliate URL"),p.text(n,"Seller Name"),
            p.enumValue(p.select(n,"Seller Level"),SellerLevel.class,SellerLevel.UNKNOWN),p.decimal(n,"Price Min"),p.decimal(n,"Price Max"),p.decimal(n,"Estimated AOV"),p.decimal(n,"Rating"),
            p.integer(n,"Review Count"),p.integer(n,"Delivery Days"),p.integer(n,"Orders in Queue"),p.multiSelect(n,"Target Audience"),p.text(n,"Primary Pain Point"),p.integer(n,"Visual Potential"),
            p.integer(n,"Purchase Intent"),p.integer(n,"Commission Potential"),p.integer(n,"Product Quality Score"),p.enumValue(p.select(n,"Status"),ProductStatus.class,ProductStatus.DISCOVERED),
            p.multiSelect(n,"Data Source"),p.localDate(n,"Last Checked Date"),p.text(n,"Notes"),p.createdTime(n),p.updatedTime(n));}
    private String value(String v){return v==null?"":v;}
}
