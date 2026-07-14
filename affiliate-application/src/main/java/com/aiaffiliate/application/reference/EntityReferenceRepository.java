package com.aiaffiliate.application.reference;

import java.util.Optional;

/** 实体引用存储端口，后续可替换为 PostgreSQL。 */
public interface EntityReferenceRepository {
    NotionEntityReference save(NotionEntityReference reference);
    Optional<NotionEntityReference> findByDomainId(String domainId, EntityType entityType);
    Optional<NotionEntityReference> findByNotionPageId(String notionPageId);
}
