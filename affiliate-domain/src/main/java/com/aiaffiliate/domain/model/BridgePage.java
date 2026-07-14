package com.aiaffiliate.domain.model;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/** Bridge Page 内容聚合根。完整正文可由基础设施层存入 Notion Page Body。 */
public record BridgePage(
        BridgePageId id, String pageName, OpportunityId opportunityId, Set<ProductId> productIds,
        KeywordId primaryKeywordId, BridgePageType pageType, Set<String> audiences,
        MarketingChannel channel, String language, String urlSlug, URI publishedUrl,
        String headline, String subheadline, String metaTitle, String metaDescription,
        String heroCta, String primaryCta, String secondaryCta, String pageOutline,
        String fullContent, String disclosureText, Set<TrustElement> trustElements,
        String aiModel, String promptVersion, Integer contentScore, ComplianceStatus complianceStatus,
        BridgePageStatus status, LocalDate publishedDate, Instant createdAt, Instant updatedAt) {

    public BridgePage {
        Objects.requireNonNull(id, "id must not be null"); pageName = DomainRules.text(pageName, "pageName");
        Objects.requireNonNull(opportunityId, "opportunityId must not be null"); productIds = DomainRules.immutableSet(productIds);
        Objects.requireNonNull(primaryKeywordId, "primaryKeywordId must not be null"); pageType = pageType == null ? BridgePageType.UNKNOWN : pageType;
        audiences = DomainRules.immutableSet(audiences); channel = channel == null ? MarketingChannel.UNKNOWN : channel;
        language = language == null || language.isBlank() ? "en" : language.trim(); urlSlug = DomainRules.text(urlSlug, "urlSlug");
        headline = empty(headline); subheadline = empty(subheadline); metaTitle = empty(metaTitle); metaDescription = empty(metaDescription);
        heroCta = empty(heroCta); primaryCta = empty(primaryCta); secondaryCta = empty(secondaryCta); pageOutline = empty(pageOutline);
        fullContent = empty(fullContent); disclosureText = empty(disclosureText); trustElements = DomainRules.immutableSet(trustElements);
        aiModel = empty(aiModel); promptVersion = empty(promptVersion); DomainRules.optionalScore(contentScore, "contentScore");
        complianceStatus = complianceStatus == null ? ComplianceStatus.NOT_REVIEWED : complianceStatus;
        status = status == null ? BridgePageStatus.IDEA : status;
        Objects.requireNonNull(createdAt, "createdAt must not be null"); Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }
    private static String empty(String value) { return value == null ? "" : value; }
}
