package com.aiaffiliate.domain.service;

import com.aiaffiliate.domain.model.OpportunityPriority;
import com.aiaffiliate.domain.model.Score;

/** 机会综合评分和优先级判定领域服务。 */
public class OpportunityScoringService {
    public Result calculate(Integer fiverrValue, Integer pinterestDemand, Integer seoOpportunity,
                            Integer commercialIntent, Integer competition, Integer contentPotential,
                            Integer confidence) {
        int finalScore = (int) Math.round(
                Score.of(fiverrValue, "fiverrValue").value() * 0.25
                + Score.of(pinterestDemand, "pinterestDemand").value() * 0.20
                + Score.of(seoOpportunity, "seoOpportunity").value() * 0.10
                + Score.of(commercialIntent, "commercialIntent").value() * 0.20
                + (100 - Score.of(competition, "competition").value()) * 0.10
                + Score.of(contentPotential, "contentPotential").value() * 0.10
                + Score.of(confidence, "confidence").value() * 0.05);
        Score score = new Score(finalScore);
        return new Result(score, priorityFor(score.value()));
    }

    public OpportunityPriority priorityFor(int score) {
        if (score >= 85) return OpportunityPriority.P0_IMMEDIATE_TEST;
        if (score >= 75) return OpportunityPriority.P1_HIGH;
        if (score >= 65) return OpportunityPriority.P2_MEDIUM;
        if (score >= 50) return OpportunityPriority.P3_LOW;
        return OpportunityPriority.REJECT;
    }

    public record Result(Score score, OpportunityPriority priority) {}
}
