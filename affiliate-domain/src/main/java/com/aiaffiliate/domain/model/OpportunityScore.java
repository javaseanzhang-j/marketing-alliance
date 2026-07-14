package com.aiaffiliate.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 机会评分值对象。所有维度均为 0-100，综合分采用对联盟转化更敏感的固定权重计算。
 */
public record OpportunityScore(
        BigDecimal demand,
        BigDecimal competition,
        BigDecimal commissionPotential,
        BigDecimal contentFit,
        BigDecimal total) {

    public OpportunityScore {
        demand = normalize(demand, "demand");
        competition = normalize(competition, "competition");
        commissionPotential = normalize(commissionPotential, "commissionPotential");
        contentFit = normalize(contentFit, "contentFit");
        total = normalize(total, "total");
    }

    /** 根据平台统一权重创建评分，competition 值越高表示竞争优势越大。 */
    public static OpportunityScore calculate(
            BigDecimal demand,
            BigDecimal competition,
            BigDecimal commissionPotential,
            BigDecimal contentFit) {
        BigDecimal total = demand.multiply(new BigDecimal("0.30"))
                .add(competition.multiply(new BigDecimal("0.20")))
                .add(commissionPotential.multiply(new BigDecimal("0.30")))
                .add(contentFit.multiply(new BigDecimal("0.20")))
                .setScale(2, RoundingMode.HALF_UP);
        return new OpportunityScore(demand, competition, commissionPotential, contentFit, total);
    }

    private static BigDecimal normalize(BigDecimal value, String field) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0
                || value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(field + " must be between 0 and 100");
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}

