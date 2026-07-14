package com.aiaffiliate.infrastructure.provider;

import com.aiaffiliate.domain.model.Keyword;
import com.aiaffiliate.domain.model.KeywordIntent;
import com.aiaffiliate.domain.model.KeywordStatus;
import com.aiaffiliate.domain.port.SEODataProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** 第一阶段 SEO 指标数据桩。 */
@Component
public class MockSEODataProvider implements SEODataProvider {

    @Override
    public List<Keyword> expandKeywords(String seedKeyword, int limit) {
        if (seedKeyword == null || seedKeyword.isBlank()) {
            throw new IllegalArgumentException("seedKeyword must not be blank");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be positive");
        }
        String seed = seedKeyword.trim();
        return List.of(
                keyword("best " + seed, KeywordIntent.COMMERCIAL, 3200, 37),
                keyword(seed + " services", KeywordIntent.TRANSACTIONAL, 1900, 42),
                keyword("how to choose " + seed, KeywordIntent.INFORMATIONAL, 1200, 29))
                .stream().limit(limit).toList();
    }

    private static Keyword keyword(String phrase, KeywordIntent intent, long volume, int difficulty) {
        return new Keyword(UUID.nameUUIDFromBytes(phrase.getBytes()), phrase, intent, volume, difficulty,
                "mock-seo", KeywordStatus.CANDIDATE, Instant.parse("2026-01-01T00:00:00Z"));
    }
}
