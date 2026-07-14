package com.aiaffiliate.notion.registry;

import java.util.Map;

/** 初始化生成的 Database ID 注册表。 */
public interface DatabaseIdRegistry { Map<String, String> load(); void save(Map<String, String> databaseIds); }
