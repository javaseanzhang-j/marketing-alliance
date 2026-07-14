package com.aiaffiliate.notion.exception;
public class NotionPermissionException extends NotionIntegrationException { public NotionPermissionException(String message) { super(message, 403); } }
