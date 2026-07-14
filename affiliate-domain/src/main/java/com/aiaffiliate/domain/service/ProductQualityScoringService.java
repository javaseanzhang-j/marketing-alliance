package com.aiaffiliate.domain.service;

import com.aiaffiliate.domain.model.Score;

import java.math.BigDecimal;

/** 产品质量评分领域服务。 */
public class ProductQualityScoringService {
    public Score calculate(Integer commissionPotential, Integer purchaseIntent, Integer visualPotential,
                           BigDecimal rating, Integer reviewCount) {
        if (rating == null) throw new IllegalArgumentException("rating must not be null");
        if (reviewCount == null || reviewCount < 0) throw new IllegalArgumentException("reviewCount must not be negative or null");
        double ratingScore = Math.min(rating.doubleValue() * 20, 100);
        double reviewScore = Math.min(reviewCount / 10.0, 100);
        int result = (int) Math.round(
                Score.of(commissionPotential, "commissionPotential").value() * 0.30
                + Score.of(purchaseIntent, "purchaseIntent").value() * 0.25
                + Score.of(visualPotential, "visualPotential").value() * 0.20
                + ratingScore * 0.15 + reviewScore * 0.10);
        return new Score(result);
    }
}
