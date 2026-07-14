package com.aiaffiliate.notion.exception;

/** Notion 集成异常基类。 */
public class NotionIntegrationException extends RuntimeException {
    private final int statusCode;
    public NotionIntegrationException(String message, int statusCode) { super(message); this.statusCode = statusCode; }
    public NotionIntegrationException(String message, int statusCode, Throwable cause) { super(message, cause); this.statusCode = statusCode; }
    public int statusCode() { return statusCode; }
}
