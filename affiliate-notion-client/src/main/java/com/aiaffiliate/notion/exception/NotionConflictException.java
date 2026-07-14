package com.aiaffiliate.notion.exception;
public class NotionConflictException extends NotionIntegrationException { public NotionConflictException(String message) { super(message, 409); } }
