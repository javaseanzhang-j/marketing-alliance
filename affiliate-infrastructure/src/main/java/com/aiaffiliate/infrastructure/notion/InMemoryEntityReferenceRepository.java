package com.aiaffiliate.infrastructure.notion;

import com.aiaffiliate.application.reference.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** MVP 实体引用内存实现。 */
public class InMemoryEntityReferenceRepository implements EntityReferenceRepository {
    private final Map<String, NotionEntityReference> byDomain = new ConcurrentHashMap<>();
    private final Map<String, NotionEntityReference> byPage = new ConcurrentHashMap<>();
    @Override public NotionEntityReference save(NotionEntityReference reference) { byDomain.put(key(reference.domainId(), reference.entityType()), reference); byPage.put(reference.notionPageId(), reference); return reference; }
    @Override public Optional<NotionEntityReference> findByDomainId(String domainId, EntityType type) { return Optional.ofNullable(byDomain.get(key(domainId, type))); }
    @Override public Optional<NotionEntityReference> findByNotionPageId(String pageId) { return Optional.ofNullable(byPage.get(pageId)); }
    private String key(String domainId, EntityType type) { return type + ":" + domainId; }
}
