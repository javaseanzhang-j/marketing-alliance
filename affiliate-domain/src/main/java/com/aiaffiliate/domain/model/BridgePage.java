package com.aiaffiliate.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** AI 生成的 Bridge Page 内容方案。 */
public record BridgePage(
        UUID id,
        UUID productId,
        UUID keywordId,
        String headline,
        String promise,
        List<String> sections,
        String callToAction,
        ContentStatus status,
        Instant createdAt) {

    public BridgePage {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(keywordId, "keywordId must not be null");
        headline = requireText(headline, "headline");
        promise = requireText(promise, "promise");
        sections = List.copyOf(Objects.requireNonNull(sections, "sections must not be null"));
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("sections must not be empty");
        }
        callToAction = requireText(callToAction, "callToAction");
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

