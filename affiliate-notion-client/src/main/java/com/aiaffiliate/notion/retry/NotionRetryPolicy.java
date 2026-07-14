package com.aiaffiliate.notion.retry;

import com.aiaffiliate.notion.exception.NotionRateLimitException;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/** 有上限、指数退避并带随机抖动的同步重试策略。 */
public class NotionRetryPolicy {
    private final int maxRetries;
    public NotionRetryPolicy(int maxRetries) { if (maxRetries < 0) throw new IllegalArgumentException("maxRetries must not be negative"); this.maxRetries = maxRetries; }
    public int maxAttempts() { return maxRetries + 1; }
    public Duration delay(int retryIndex, RuntimeException failure) {
        if (failure instanceof NotionRateLimitException rate && rate.retryAfterSeconds() > 0) return Duration.ofSeconds(rate.retryAfterSeconds());
        long exponential = Math.min(500L * (1L << Math.min(retryIndex, 6)), 10_000L);
        return Duration.ofMillis(exponential + ThreadLocalRandom.current().nextLong(100, 401));
    }
}
