package com.aiaffiliate.domain.port;

import java.util.List;

/** Pinterest 趋势数据入口；为后续官方 API 集成预留。 */
public interface PinterestDataProvider {
    List<String> trendingTopics(String keyword, int limit);
}

