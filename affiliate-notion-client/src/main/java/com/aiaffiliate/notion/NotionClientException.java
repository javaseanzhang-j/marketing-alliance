package com.aiaffiliate.notion;

/** Notion 网络、序列化或非成功响应的统一异常。 */
public class NotionClientException extends RuntimeException {

    public NotionClientException(String message) {
        super(message);
    }

    public NotionClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

