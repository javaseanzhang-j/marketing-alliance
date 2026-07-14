package com.aiaffiliate.domain.model;

import java.util.UUID;

/** 联盟产品的领域标识。 */
public record ProductId(String value) {
    public ProductId { if (value == null || value.isBlank()) throw new IllegalArgumentException("ProductId must not be blank"); value = value.trim(); }
    public static ProductId newId() { return new ProductId(UUID.randomUUID().toString()); }
}
