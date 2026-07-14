package com.aiaffiliate.application.notion;

import java.util.List;

/** 管理端可调用的 Notion 初始化与 Schema 查询用例。 */
public interface NotionAdministrationUseCase {
    List<SchemaStatus> validateSchemas();
    InitializationResult initialize();
}
