package com.aiaffiliate.infrastructure.notion.mapper;

import com.aiaffiliate.application.reference.*;
import com.aiaffiliate.domain.model.*;
import com.aiaffiliate.notion.mapper.NotionPropertyMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

/** Keyword 与 Notion Page 映射。 */
public class KeywordNotionMapper extends AbstractNotionMapper<Keyword> {
    public KeywordNotionMapper(NotionPropertyMapper p, EntityReferenceRepository references) { super(p, references); }
    public String domainId(Keyword value) { return value.id().value(); } public EntityType entityType() { return EntityType.KEYWORD; }
    public Map<String, Object> toCreateProperties(Keyword v) { return properties(v); } public Map<String, Object> toUpdateProperties(Keyword v) { return properties(v); }
    private Map<String, Object> properties(Keyword v) { Map<String,Object> m = new LinkedHashMap<>();
        m.put("Keyword",p.title(v.value())); m.put("Keyword ID",p.richText(v.id().value())); m.put("Category",p.select(v.category())); m.put("Subcategory",p.select(v.subcategory()));
        m.put("Intent",p.select(v.intent())); m.put("Source",p.multiSelect(v.sources())); m.put("Language",p.select(v.language())); m.put("Country",p.multiSelect(v.countries()));
        m.put("Search Volume",p.number(v.searchVolume())); m.put("Pinterest Demand",p.number(v.pinterestDemand())); m.put("Commercial Intent",p.number(v.commercialIntent()));
        m.put("Competition Score",p.number(v.competitionScore())); m.put("Trend Score",p.number(v.trendScore())); m.put("Keyword Score",p.number(v.keywordScore()));
        m.put("Status",p.select(v.status())); m.put("Notes",p.richText(v.notes())); return managed(m); }
    public Keyword fromPage(JsonNode n) { return new Keyword(new KeywordId(p.text(n,"Keyword ID")),p.text(n,"Keyword"),p.select(n,"Category"),p.select(n,"Subcategory"),
            p.enumValue(p.select(n,"Intent"),KeywordIntent.class,KeywordIntent.UNKNOWN),enums(p.multiSelect(n,"Source"),KeywordSource.class,KeywordSource.UNKNOWN),
            value(p.select(n,"Language"),"en"),p.multiSelect(n,"Country"),p.integer(n,"Search Volume"),p.integer(n,"Pinterest Demand"),p.integer(n,"Commercial Intent"),
            p.integer(n,"Competition Score"),p.integer(n,"Trend Score"),p.integer(n,"Keyword Score"),p.enumValue(p.select(n,"Status"),KeywordStatus.class,KeywordStatus.INBOX),
            p.text(n,"Notes"),p.createdTime(n),p.updatedTime(n)); }
    private <E extends Enum<E>> Set<E> enums(Set<String> values, Class<E> type, E unknown) { Set<E> result=new LinkedHashSet<>(); values.forEach(v->result.add(p.enumValue(v,type,unknown))); return result; }
    private String value(String value,String fallback){return value==null?fallback:value;}
}
