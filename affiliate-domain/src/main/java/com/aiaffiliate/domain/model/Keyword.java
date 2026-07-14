package com.aiaffiliate.domain.model;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/** 联盟营销关键词实体，包含可用于筛选和排序的 SEO 指标。 */
public record Keyword(
        UUID id,
        String phrase,
        KeywordIntent intent,
        long monthlySearchVolume,
        int difficulty,
        String source,
        KeywordStatus status,
        Instant createdAt) {

    public Keyword {
        Objects.requireNonNull(id, "id must not be null");
        phrase = requireText(phrase, "phrase").toLowerCase(Locale.ROOT);
        Objects.requireNonNull(intent, "intent must not be null");
        if (monthlySearchVolume < 0) {
            throw new IllegalArgumentException("monthlySearchVolume must not be negative");
        }
        if (difficulty < 0 || difficulty > 100) {
            throw new IllegalArgumentException("difficulty must be between 0 and 100");
        }
        source = requireText(source, "source");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}

