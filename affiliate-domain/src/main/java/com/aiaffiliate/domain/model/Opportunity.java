package com.aiaffiliate.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/** 联盟营销机会聚合根。 */
public record Opportunity(
        OpportunityId id, String opportunityName, ProductId productId, KeywordId primaryKeywordId,
        Set<KeywordId> secondaryKeywordIds, MarketingChannel channel, Set<String> audiences,
        Set<String> countries, FunnelStage funnelStage, ContentAngle contentAngle,
        Integer fiverrValueScore, Integer pinterestDemandScore, Integer seoOpportunityScore,
        Integer commercialIntentScore, Integer competitionScore, Integer contentPotentialScore,
        Integer confidenceScore, Integer finalScore, OpportunityPriority priority,
        OpportunityRecommendation recommendation, String aiSummary, String keyEvidence, String risks,
        BigDecimal testBudget, OpportunityStatus status, Instant createdAt, Instant updatedAt) {

    public Opportunity {
        Objects.requireNonNull(id, "id must not be null"); opportunityName = DomainRules.text(opportunityName, "opportunityName");
        Objects.requireNonNull(productId, "productId must not be null"); Objects.requireNonNull(primaryKeywordId, "primaryKeywordId must not be null");
        secondaryKeywordIds = DomainRules.immutableSet(secondaryKeywordIds); channel = channel == null ? MarketingChannel.UNKNOWN : channel;
        audiences = DomainRules.immutableSet(audiences); countries = DomainRules.immutableSet(countries);
        funnelStage = funnelStage == null ? FunnelStage.UNKNOWN : funnelStage; contentAngle = contentAngle == null ? ContentAngle.UNKNOWN : contentAngle;
        DomainRules.optionalScore(fiverrValueScore, "fiverrValueScore"); DomainRules.optionalScore(pinterestDemandScore, "pinterestDemandScore");
        DomainRules.optionalScore(seoOpportunityScore, "seoOpportunityScore"); DomainRules.optionalScore(commercialIntentScore, "commercialIntentScore");
        DomainRules.optionalScore(competitionScore, "competitionScore"); DomainRules.optionalScore(contentPotentialScore, "contentPotentialScore");
        DomainRules.optionalScore(confidenceScore, "confidenceScore"); DomainRules.optionalScore(finalScore, "finalScore");
        priority = priority == null ? OpportunityPriority.REJECT : priority; recommendation = recommendation == null ? OpportunityRecommendation.UNKNOWN : recommendation;
        aiSummary = aiSummary == null ? "" : aiSummary; keyEvidence = keyEvidence == null ? "" : keyEvidence; risks = risks == null ? "" : risks;
        if (testBudget != null && testBudget.signum() < 0) throw new IllegalArgumentException("testBudget must not be negative");
        status = status == null ? OpportunityStatus.DRAFT : status;
        Objects.requireNonNull(createdAt, "createdAt must not be null"); Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }
}
