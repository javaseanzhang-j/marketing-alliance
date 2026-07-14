package com.aiaffiliate.application.reference;

import java.time.Instant;

/** 领域 ID 和 Notion Page ID 的独立映射。 */
public record NotionEntityReference(String domainId, String notionPageId, EntityType entityType,
                                    Instant createdAt, Instant updatedAt) {
    public NotionEntityReference {
        if (domainId == null || domainId.isBlank()) throw new IllegalArgumentException("domainId must not be blank");
        if (notionPageId == null || notionPageId.isBlank()) throw new IllegalArgumentException("notionPageId must not be blank");
        if (entityType == null) throw new IllegalArgumentException("entityType must not be null");
        if (createdAt == null || updatedAt == null) throw new IllegalArgumentException("timestamps must not be null");
    }
}
