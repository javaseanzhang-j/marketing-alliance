package com.aiaffiliate.domain.service;

import com.aiaffiliate.domain.model.OpportunityPriority;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ScoringServicesTest {
    @Test void calculatesKeywordScore() {
        assertEquals(71, new KeywordScoringService().calculate(80, 70, 60, 30).value());
    }
    @Test void calculatesProductQualityScore() {
        assertEquals(76, new ProductQualityScoringService().calculate(80, 70, 90, new BigDecimal("4.5"), 250).value());
    }
    @Test void calculatesOpportunityPriority() {
        var result = new OpportunityScoringService().calculate(90, 90, 80, 90, 20, 85, 90);
        assertEquals(88, result.score().value());
        assertEquals(OpportunityPriority.P0_IMMEDIATE_TEST, result.priority());
    }
    @Test void rejectsMissingInputsExplicitly() {
        assertThrows(IllegalArgumentException.class, () -> new KeywordScoringService().calculate(null, 1, 1, 1));
    }
}
