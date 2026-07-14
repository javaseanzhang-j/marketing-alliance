package com.aiaffiliate.domain.model;

import java.util.UUID;

/** Pinterest 内容的领域标识。 */
public record PinterestContentId(String value) {
    public PinterestContentId { if (value == null || value.isBlank()) throw new IllegalArgumentException("PinterestContentId must not be blank"); value = value.trim(); }
    public static PinterestContentId newId() { return new PinterestContentId(UUID.randomUUID().toString()); }
}
