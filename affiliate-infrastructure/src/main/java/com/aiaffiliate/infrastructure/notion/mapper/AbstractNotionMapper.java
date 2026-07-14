package com.aiaffiliate.infrastructure.notion.mapper;

import com.aiaffiliate.application.reference.*;
import com.aiaffiliate.notion.mapper.NotionPropertyMapper;
import java.util.*;

/** Relation ID 转换和公共托管字段映射。 */
abstract class AbstractNotionMapper<T> implements NotionEntityMapper<T> {
    protected final NotionPropertyMapper p; protected final EntityReferenceRepository references;
    protected AbstractNotionMapper(NotionPropertyMapper p, EntityReferenceRepository references) { this.p = p; this.references = references; }
    protected Map<String, Object> managed(Map<String, Object> properties) { properties.put("Schema Version", p.richText("1.0")); properties.put("Managed By", p.richText("AI Affiliate Platform")); return properties; }
    protected Set<String> pageIds(Collection<String> domainIds, EntityType type) {
        if (domainIds == null) return Set.of(); Set<String> result = new LinkedHashSet<>();
        domainIds.forEach(id -> references.findByDomainId(id, type).map(NotionEntityReference::notionPageId).ifPresent(result::add)); return result;
    }
    protected String domainId(String notionPageId, EntityType type) {
        return references.findByNotionPageId(notionPageId).filter(ref -> ref.entityType() == type).map(NotionEntityReference::domainId).orElse(notionPageId);
    }
    protected String summarize(String content) { if (content == null) return ""; return content.length() <= 1900 ? content : content.substring(0, 1900) + "…"; }
}
