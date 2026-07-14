package com.aiaffiliate.domain.model;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Fiverr 联盟产品聚合根。该对象只保存平台无关的业务信息，外部数据格式由基础设施层转换。
 */
public record Product(
        UUID id,
        String externalId,
        String name,
        String category,
        String seller,
        BigDecimal startingPrice,
        String currency,
        URI affiliateUrl,
        String summary,
        ProductStatus status,
        Instant discoveredAt) {

    public Product {
        Objects.requireNonNull(id, "id must not be null");
        externalId = requireText(externalId, "externalId");
        name = requireText(name, "name");
        category = requireText(category, "category");
        seller = requireText(seller, "seller");
        Objects.requireNonNull(startingPrice, "startingPrice must not be null");
        if (startingPrice.signum() < 0) {
            throw new IllegalArgumentException("startingPrice must not be negative");
        }
        currency = requireText(currency, "currency").toUpperCase();
        Objects.requireNonNull(affiliateUrl, "affiliateUrl must not be null");
        summary = requireText(summary, "summary");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(discoveredAt, "discoveredAt must not be null");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}

