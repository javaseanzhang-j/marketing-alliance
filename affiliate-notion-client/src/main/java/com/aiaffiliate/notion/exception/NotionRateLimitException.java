package com.aiaffiliate.notion.exception;
public class NotionRateLimitException extends NotionIntegrationException {
    private final long retryAfterSeconds;
    public NotionRateLimitException(String message, long retryAfterSeconds) { super(message, 429); this.retryAfterSeconds = retryAfterSeconds; }
    public long retryAfterSeconds() { return retryAfterSeconds; }
}
