package com.aiaffiliate.domain.model;

import java.util.UUID;

/** 营销机会的领域标识。 */
public record OpportunityId(String value) {
    public OpportunityId { if (value == null || value.isBlank()) throw new IllegalArgumentException("OpportunityId must not be blank"); value = value.trim(); }
    public static OpportunityId newId() { return new OpportunityId(UUID.randomUUID().toString()); }
}
