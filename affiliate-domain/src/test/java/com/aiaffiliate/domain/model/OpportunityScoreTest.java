package com.aiaffiliate.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OpportunityScoreTest {

    @Test
    void calculatesWeightedTotal() {
        OpportunityScore score = OpportunityScore.calculate(
                new BigDecimal("80"), new BigDecimal("70"),
                new BigDecimal("90"), new BigDecimal("85"));

        assertEquals(new BigDecimal("82.00"), score.total());
    }

    @Test
    void rejectsOutOfRangeDimension() {
        assertThrows(IllegalArgumentException.class, () -> OpportunityScore.calculate(
                new BigDecimal("101"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
    }
}
