package com.aiaffiliate.infrastructure.notion.mapper;

import com.aiaffiliate.application.reference.*;
import com.aiaffiliate.domain.model.*;
import com.aiaffiliate.notion.mapper.NotionPropertyMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/** PinterestContent 与 Notion Page 映射。 */
public class PinterestContentNotionMapper extends AbstractNotionMapper<PinterestContent>{
    public PinterestContentNotionMapper(NotionPropertyMapper p,EntityReferenceRepository r){super(p,r);}public String domainId(PinterestContent v){return v.id().value();}public EntityType entityType(){return EntityType.PINTEREST_CONTENT;}
    public Map<String,Object>toCreateProperties(PinterestContent v){return props(v);}public Map<String,Object>toUpdateProperties(PinterestContent v){return props(v);}
    private Map<String,Object>props(PinterestContent v){Map<String,Object>m=new LinkedHashMap<>();m.put("Pin Name",p.title(v.pinName()));m.put("Pin ID",p.richText(v.id().value()));
        m.put("Opportunity",p.relation(pageIds(List.of(v.opportunityId().value()),EntityType.OPPORTUNITY)));m.put("Bridge Page",p.relation(pageIds(List.of(v.bridgePageId().value()),EntityType.BRIDGE_PAGE)));m.put("Product",p.relation(pageIds(List.of(v.productId().value()),EntityType.AFFILIATE_PRODUCT)));m.put("Primary Keyword",p.relation(pageIds(List.of(v.primaryKeywordId().value()),EntityType.KEYWORD)));
        m.put("Pin Type",p.select(v.pinType()));m.put("Creative Angle",p.select(v.creativeAngle()));m.put("Pin Title",p.richText(v.pinTitle()));m.put("Pin Description",p.richText(v.pinDescription()));m.put("Overlay Text",p.richText(v.overlayText()));m.put("Image Prompt",p.richText(v.imagePrompt()));m.put("Design Template",p.select(v.designTemplate()));m.put("Asset URL",p.url(v.assetUrl()));m.put("Destination URL",p.url(v.destinationUrl()));
        m.put("UTM Source",p.richText(v.utmSource()));m.put("UTM Medium",p.richText(v.utmMedium()));m.put("UTM Campaign",p.richText(v.utmCampaign()));m.put("UTM Content",p.richText(v.utmContent()));m.put("Final URL",p.url(v.finalUrl()));m.put("Board",p.select(v.board()));m.put("Publishing Type",p.select(v.publishingType()));m.put("Publish Date",p.date(v.publishDate()));m.put("Published URL",p.url(v.publishedUrl()));m.put("Status",p.select(v.status()));m.put("Compliance Status",p.select(v.complianceStatus()));return managed(m);}
    public PinterestContent fromPage(JsonNode n){return new PinterestContent(new PinterestContentId(p.text(n,"Pin ID")),p.text(n,"Pin Name"),new OpportunityId(domainId(first(p.relationIds(n,"Opportunity")),EntityType.OPPORTUNITY)),new BridgePageId(domainId(first(p.relationIds(n,"Bridge Page")),EntityType.BRIDGE_PAGE)),new ProductId(domainId(first(p.relationIds(n,"Product")),EntityType.AFFILIATE_PRODUCT)),new KeywordId(domainId(first(p.relationIds(n,"Primary Keyword")),EntityType.KEYWORD)),
            p.enumValue(p.select(n,"Pin Type"),PinType.class,PinType.UNKNOWN),p.enumValue(p.select(n,"Creative Angle"),CreativeAngle.class,CreativeAngle.UNKNOWN),p.text(n,"Pin Title"),p.text(n,"Pin Description"),p.text(n,"Overlay Text"),p.text(n,"Image Prompt"),value(p.select(n,"Design Template")),p.uri(n,"Asset URL"),p.uri(n,"Destination URL"),p.text(n,"UTM Source"),p.text(n,"UTM Medium"),p.text(n,"UTM Campaign"),p.text(n,"UTM Content"),p.uri(n,"Final URL"),value(p.select(n,"Board")),p.enumValue(p.select(n,"Publishing Type"),PublishingType.class,PublishingType.UNKNOWN),p.offsetDateTime(n,"Publish Date"),p.uri(n,"Published URL"),p.enumValue(p.select(n,"Status"),PinterestContentStatus.class,PinterestContentStatus.IDEA),p.enumValue(p.select(n,"Compliance Status"),ComplianceStatus.class,ComplianceStatus.NOT_REVIEWED),p.createdTime(n),p.updatedTime(n));}
    private String first(Set<String>s){if(s.isEmpty())throw new IllegalArgumentException("Required relation is empty");return s.iterator().next();}private String value(String v){return v==null?"":v;}
}
