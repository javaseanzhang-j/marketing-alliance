package com.aiaffiliate.notion.client;

/** 通用 Notion API Client，组合 Database 和 Page 能力。 */
public interface NotionClient extends NotionDatabaseClient, NotionPageClient {
    default JsonNodePropertyFactory properties() { return new JsonNodePropertyFactory(); }
}
