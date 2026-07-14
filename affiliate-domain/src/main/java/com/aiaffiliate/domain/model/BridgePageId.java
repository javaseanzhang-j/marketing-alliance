package com.aiaffiliate.domain.model;

import java.util.UUID;

/** Bridge Page 的领域标识。 */
public record BridgePageId(String value) {
    public BridgePageId { if (value == null || value.isBlank()) throw new IllegalArgumentException("BridgePageId must not be blank"); value = value.trim(); }
    public static BridgePageId newId() { return new BridgePageId(UUID.randomUUID().toString()); }
}
