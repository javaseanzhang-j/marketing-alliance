package com.aiaffiliate.infrastructure.provider;

import com.aiaffiliate.domain.model.*;
import com.aiaffiliate.domain.port.SEODataProvider;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.*;

/** 第一阶段 SEO 指标数据桩。 */
@Component
public class MockSEODataProvider implements SEODataProvider {
    public List<Keyword>expandKeywords(String seed,int limit){if(seed==null||seed.isBlank())throw new IllegalArgumentException("seedKeyword must not be blank");if(limit<1)throw new IllegalArgumentException("limit must be positive");return List.of(keyword("best "+seed,KeywordIntent.COMMERCIAL,3200,37),keyword(seed+" services",KeywordIntent.TRANSACTIONAL,1900,42),keyword("how to choose "+seed,KeywordIntent.INFORMATIONAL,1200,29)).stream().limit(limit).toList();}
    private Keyword keyword(String value,KeywordIntent intent,int volume,int competition){Instant now=Instant.parse("2026-01-01T00:00:00Z");return new Keyword(new KeywordId(UUID.nameUUIDFromBytes(value.getBytes()).toString()),value,"Fiverr","",intent,Set.of(KeywordSource.AI_GENERATED),"en",Set.of("US"),volume,75,80,competition,70,73,KeywordStatus.INBOX,"Mock SEO keyword",now,now);}
}
