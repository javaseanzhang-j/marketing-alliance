package com.aiaffiliate.infrastructure.notion.mapper;

import com.aiaffiliate.application.reference.*;
import com.aiaffiliate.domain.model.*;
import com.aiaffiliate.notion.mapper.NotionPropertyMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/** BridgePage 与 Notion Page 映射；完整正文写入 Page Body。 */
public class BridgePageNotionMapper extends AbstractNotionMapper<BridgePage>{
    public BridgePageNotionMapper(NotionPropertyMapper p,EntityReferenceRepository r){super(p,r);}public String domainId(BridgePage v){return v.id().value();}public EntityType entityType(){return EntityType.BRIDGE_PAGE;}
    public Map<String,Object>toCreateProperties(BridgePage v){return props(v);}public Map<String,Object>toUpdateProperties(BridgePage v){return props(v);}public List<Map<String,Object>>toPageBody(BridgePage v){return p.pageBodyBlocks(v.fullContent());}
    private Map<String,Object>props(BridgePage v){Map<String,Object>m=new LinkedHashMap<>();m.put("Page Name",p.title(v.pageName()));m.put("Bridge Page ID",p.richText(v.id().value()));
        m.put("Opportunity",p.relation(pageIds(List.of(v.opportunityId().value()),EntityType.OPPORTUNITY)));m.put("Products",p.relation(pageIds(v.productIds().stream().map(ProductId::value).toList(),EntityType.AFFILIATE_PRODUCT)));
        m.put("Primary Keyword",p.relation(pageIds(List.of(v.primaryKeywordId().value()),EntityType.KEYWORD)));m.put("Page Type",p.select(v.pageType()));m.put("Audience",p.multiSelect(v.audiences()));m.put("Channel",p.select(v.channel()));m.put("Language",p.select(v.language()));
        m.put("URL Slug",p.richText(v.urlSlug()));m.put("Published URL",p.url(v.publishedUrl()));m.put("Headline",p.richText(v.headline()));m.put("Subheadline",p.richText(v.subheadline()));m.put("Meta Title",p.richText(v.metaTitle()));m.put("Meta Description",p.richText(v.metaDescription()));
        m.put("Hero CTA",p.richText(v.heroCta()));m.put("Primary CTA",p.richText(v.primaryCta()));m.put("Secondary CTA",p.richText(v.secondaryCta()));m.put("Page Outline",p.richText(v.pageOutline()));m.put("Full Content",p.richText(summarize(v.fullContent())));m.put("Disclosure Text",p.richText(v.disclosureText()));
        m.put("Trust Elements",p.multiSelect(v.trustElements()));m.put("AI Model",p.select(v.aiModel()));m.put("Prompt Version",p.richText(v.promptVersion()));m.put("Content Score",p.number(v.contentScore()));m.put("Compliance Status",p.select(v.complianceStatus()));m.put("Status",p.select(v.status()));m.put("Published Date",p.date(v.publishedDate()));return managed(m);}
    public BridgePage fromPage(JsonNode n){Set<ProductId>products=new LinkedHashSet<>();p.relationIds(n,"Products").forEach(id->products.add(new ProductId(domainId(id,EntityType.AFFILIATE_PRODUCT))));Set<TrustElement>trust=new LinkedHashSet<>();p.multiSelect(n,"Trust Elements").forEach(v->trust.add(p.enumValue(v,TrustElement.class,TrustElement.UNKNOWN)));
        return new BridgePage(new BridgePageId(p.text(n,"Bridge Page ID")),p.text(n,"Page Name"),new OpportunityId(domainId(first(p.relationIds(n,"Opportunity")),EntityType.OPPORTUNITY)),products,new KeywordId(domainId(first(p.relationIds(n,"Primary Keyword")),EntityType.KEYWORD)),
                p.enumValue(p.select(n,"Page Type"),BridgePageType.class,BridgePageType.UNKNOWN),p.multiSelect(n,"Audience"),p.enumValue(p.select(n,"Channel"),MarketingChannel.class,MarketingChannel.UNKNOWN),value(p.select(n,"Language"),"en"),p.text(n,"URL Slug"),p.uri(n,"Published URL"),
                p.text(n,"Headline"),p.text(n,"Subheadline"),p.text(n,"Meta Title"),p.text(n,"Meta Description"),p.text(n,"Hero CTA"),p.text(n,"Primary CTA"),p.text(n,"Secondary CTA"),p.text(n,"Page Outline"),p.text(n,"Full Content"),p.text(n,"Disclosure Text"),trust,
                value(p.select(n,"AI Model"),""),p.text(n,"Prompt Version"),p.integer(n,"Content Score"),p.enumValue(p.select(n,"Compliance Status"),ComplianceStatus.class,ComplianceStatus.NOT_REVIEWED),p.enumValue(p.select(n,"Status"),BridgePageStatus.class,BridgePageStatus.IDEA),p.localDate(n,"Published Date"),p.createdTime(n),p.updatedTime(n));}
    private String first(Set<String>s){if(s.isEmpty())throw new IllegalArgumentException("Required relation is empty");return s.iterator().next();}private String value(String v,String f){return v==null?f:v;}
}
