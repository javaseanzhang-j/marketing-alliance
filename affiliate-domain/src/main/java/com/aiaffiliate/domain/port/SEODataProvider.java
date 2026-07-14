package com.aiaffiliate.domain.port;

import com.aiaffiliate.domain.model.Keyword;

import java.util.List;

/** SEO 关键词数据入口；基础设施层负责接入具体供应商。 */
public interface SEODataProvider {
    List<Keyword> expandKeywords(String seedKeyword, int limit);
}

