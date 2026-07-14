package com.aiaffiliate.domain.model;

/** 0 到 100 的评分值对象。 */
public record Score(int value) {
    public Score {
        if (value < 0 || value > 100) throw new IllegalArgumentException("score must be between 0 and 100");
    }
    public static Score of(Integer value, String field) { return new Score(DomainRules.score(value, field)); }
}
