package com.aiaffiliate.infrastructure.provider;

import com.aiaffiliate.domain.port.PinterestDataProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/** 第一阶段 Pinterest 趋势数据桩。 */
@Component
public class MockPinterestDataProvider implements PinterestDataProvider {

    @Override
    public List<String> trendingTopics(String keyword, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be positive");
        }
        String seed = keyword == null || keyword.isBlank() ? "digital services" : keyword.trim();
        return List.of(seed + " ideas", seed + " checklist", seed + " before and after").stream()
                .limit(limit)
                .toList();
    }
}

