package com.aiaffiliate.domain.model;

import java.util.Objects;
import java.util.UUID;

/** 关键词的领域标识。 */
public record KeywordId(String value) {
    public KeywordId { value = requireValue(value); }
    public static KeywordId newId() { return new KeywordId(UUID.randomUUID().toString()); }
    private static String requireValue(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("KeywordId must not be blank");
        return Objects.requireNonNull(value).trim();
    }
}
