package com.aiaffiliate.domain.service;

import com.aiaffiliate.domain.model.Score;

/** 关键词评分领域服务；Java 计算结果是业务真源。 */
public class KeywordScoringService {
    public Score calculate(Integer pinterestDemand, Integer commercialIntent, Integer trendScore, Integer competitionScore) {
        int result = (int) Math.round(
                Score.of(pinterestDemand, "pinterestDemand").value() * 0.25
                + Score.of(commercialIntent, "commercialIntent").value() * 0.30
                + Score.of(trendScore, "trendScore").value() * 0.20
                + (100 - Score.of(competitionScore, "competitionScore").value()) * 0.25);
        return new Score(result);
    }
}
