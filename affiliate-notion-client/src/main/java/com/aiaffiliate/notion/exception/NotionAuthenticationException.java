package com.aiaffiliate.notion.exception;
public class NotionAuthenticationException extends NotionIntegrationException { public NotionAuthenticationException(String message) { super(message, 401); } }
