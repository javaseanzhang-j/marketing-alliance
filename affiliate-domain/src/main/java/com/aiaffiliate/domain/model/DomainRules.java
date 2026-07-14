package com.aiaffiliate.domain.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/** 领域对象通用不变量校验。 */
public final class DomainRules {
    private DomainRules() {}

    public static String text(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " must not be blank");
        return value.trim();
    }

    public static Integer optionalScore(Integer value, String field) {
        if (value != null && (value < 0 || value > 100)) throw new IllegalArgumentException(field + " must be between 0 and 100");
        return value;
    }

    public static int score(Integer value, String field) {
        if (value == null) throw new IllegalArgumentException(field + " must not be null");
        optionalScore(value, field);
        return value;
    }

    public static <T> Set<T> immutableSet(Set<T> values) {
        return values == null ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(values));
    }
}
