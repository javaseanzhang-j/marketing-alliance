package com.aiaffiliate.infrastructure.notion.mapper;

import com.aiaffiliate.application.reference.*;
import com.aiaffiliate.domain.model.*;
import com.aiaffiliate.notion.mapper.NotionPropertyMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/** Opportunity 与 Notion Page 映射。 */
public class OpportunityNotionMapper extends AbstractNotionMapper<Opportunity>{
    public OpportunityNotionMapper(NotionPropertyMapper p,EntityReferenceRepository r){super(p,r);}public String domainId(Opportunity v){return v.id().value();}public EntityType entityType(){return EntityType.OPPORTUNITY;}
    public Map<String,Object> toCreateProperties(Opportunity v){return props(v);}public Map<String,Object> toUpdateProperties(Opportunity v){return props(v);}
    private Map<String,Object> props(Opportunity v){Map<String,Object>m=new LinkedHashMap<>();m.put("Opportunity Name",p.title(v.opportunityName()));m.put("Opportunity ID",p.richText(v.id().value()));
        m.put("Product",p.relation(pageIds(List.of(v.productId().value()),EntityType.AFFILIATE_PRODUCT)));m.put("Primary Keyword",p.relation(pageIds(List.of(v.primaryKeywordId().value()),EntityType.KEYWORD)));
        m.put("Secondary Keywords",p.relation(pageIds(v.secondaryKeywordIds().stream().map(KeywordId::value).toList(),EntityType.KEYWORD)));m.put("Channel",p.select(v.channel()));m.put("Audience",p.multiSelect(v.audiences()));m.put("Country",p.multiSelect(v.countries()));
        m.put("Funnel Stage",p.select(v.funnelStage()));m.put("Content Angle",p.select(v.contentAngle()));m.put("Fiverr Value Score",p.number(v.fiverrValueScore()));m.put("Pinterest Demand Score",p.number(v.pinterestDemandScore()));
        m.put("SEO Opportunity Score",p.number(v.seoOpportunityScore()));m.put("Commercial Intent Score",p.number(v.commercialIntentScore()));m.put("Competition Score",p.number(v.competitionScore()));
        m.put("Content Potential Score",p.number(v.contentPotentialScore()));m.put("Confidence Score",p.number(v.confidenceScore()));m.put("Final Score",p.number(v.finalScore()));m.put("Priority",p.select(v.priority()));
        m.put("Recommendation",p.select(v.recommendation()));m.put("AI Summary",p.richText(v.aiSummary()));m.put("Key Evidence",p.richText(v.keyEvidence()));m.put("Risks",p.richText(v.risks()));m.put("Test Budget",p.number(v.testBudget()));m.put("Status",p.select(v.status()));return managed(m);}
    public Opportunity fromPage(JsonNode n){Set<String>product=p.relationIds(n,"Product"),primary=p.relationIds(n,"Primary Keyword");Set<KeywordId>secondary=new LinkedHashSet<>();p.relationIds(n,"Secondary Keywords").forEach(id->secondary.add(new KeywordId(domainId(id,EntityType.KEYWORD))));
        return new Opportunity(new OpportunityId(p.text(n,"Opportunity ID")),p.text(n,"Opportunity Name"),new ProductId(domainId(first(product),EntityType.AFFILIATE_PRODUCT)),new KeywordId(domainId(first(primary),EntityType.KEYWORD)),secondary,
                p.enumValue(p.select(n,"Channel"),MarketingChannel.class,MarketingChannel.UNKNOWN),p.multiSelect(n,"Audience"),p.multiSelect(n,"Country"),p.enumValue(p.select(n,"Funnel Stage"),FunnelStage.class,FunnelStage.UNKNOWN),
                p.enumValue(p.select(n,"Content Angle"),ContentAngle.class,ContentAngle.UNKNOWN),p.integer(n,"Fiverr Value Score"),p.integer(n,"Pinterest Demand Score"),p.integer(n,"SEO Opportunity Score"),p.integer(n,"Commercial Intent Score"),
                p.integer(n,"Competition Score"),p.integer(n,"Content Potential Score"),p.integer(n,"Confidence Score"),p.integer(n,"Final Score"),p.enumValue(p.select(n,"Priority"),OpportunityPriority.class,OpportunityPriority.REJECT),
                p.enumValue(p.select(n,"Recommendation"),OpportunityRecommendation.class,OpportunityRecommendation.UNKNOWN),p.text(n,"AI Summary"),p.text(n,"Key Evidence"),p.text(n,"Risks"),p.decimal(n,"Test Budget"),
                p.enumValue(p.select(n,"Status"),OpportunityStatus.class,OpportunityStatus.DRAFT),p.createdTime(n),p.updatedTime(n));}
    private String first(Set<String>s){if(s.isEmpty())throw new IllegalArgumentException("Required relation is empty");return s.iterator().next();}
}
