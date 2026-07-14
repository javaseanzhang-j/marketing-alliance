package com.aiaffiliate.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** 面向 Pinterest 的营销内容草稿。 */
public record PinterestContent(
        UUID id,
        UUID productId,
        String title,
        String description,
        String imagePrompt,
        List<String> hashtags,
        ContentStatus status,
        Instant createdAt) {

    public PinterestContent {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        title = requireText(title, "title");
        description = requireText(description, "description");
        imagePrompt = requireText(imagePrompt, "imagePrompt");
        hashtags = List.copyOf(Objects.requireNonNull(hashtags, "hashtags must not be null"));
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

