package com.aiaffiliate.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/** 关键词聚合根。评分由 KeywordScoringService 计算后写入。 */
public record Keyword(
        KeywordId id, String value, String category, String subcategory, KeywordIntent intent,
        Set<KeywordSource> sources, String language, Set<String> countries, Integer searchVolume,
        Integer pinterestDemand, Integer commercialIntent, Integer competitionScore, Integer trendScore,
        Integer keywordScore, KeywordStatus status, String notes, Instant createdAt, Instant updatedAt) {

    public Keyword {
        Objects.requireNonNull(id, "id must not be null");
        value = DomainRules.text(value, "value");
        category = category == null ? "" : category.trim();
        subcategory = subcategory == null ? "" : subcategory.trim();
        intent = intent == null ? KeywordIntent.UNKNOWN : intent;
        sources = DomainRules.immutableSet(sources);
        language = language == null || language.isBlank() ? "en" : language.trim();
        countries = DomainRules.immutableSet(countries);
        if (searchVolume != null && searchVolume < 0) throw new IllegalArgumentException("searchVolume must not be negative");
        DomainRules.optionalScore(pinterestDemand, "pinterestDemand");
        DomainRules.optionalScore(commercialIntent, "commercialIntent");
        DomainRules.optionalScore(competitionScore, "competitionScore");
        DomainRules.optionalScore(trendScore, "trendScore");
        DomainRules.optionalScore(keywordScore, "keywordScore");
        status = status == null ? KeywordStatus.INBOX : status;
        notes = notes == null ? "" : notes;
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        if (updatedAt.isBefore(createdAt)) throw new IllegalArgumentException("updatedAt must not be before createdAt");
    }
}
