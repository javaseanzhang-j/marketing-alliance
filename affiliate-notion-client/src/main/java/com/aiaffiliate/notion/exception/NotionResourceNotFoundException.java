package com.aiaffiliate.notion.exception;
public class NotionResourceNotFoundException extends NotionIntegrationException { public NotionResourceNotFoundException(String message) { super(message, 404); } }
