package com.aiaffiliate.infrastructure.provider;

import com.aiaffiliate.domain.model.Product;
import com.aiaffiliate.domain.model.ProductStatus;
import com.aiaffiliate.domain.port.FiverrDataProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/** 第一阶段 Fiverr 数据桩，确保业务流程在没有爬虫时也可联调。 */
@Component
public class MockFiverrDataProvider implements FiverrDataProvider {

    private static final List<Product> CATALOG = List.of(
            product("fvr-001", "Minimal logo design", "Graphics & Design", "northstudio", "25.00"),
            product("fvr-002", "WordPress speed optimization", "Programming & Tech", "speedcraft", "40.00"),
            product("fvr-003", "SEO blog content package", "Writing & Translation", "wordfoundry", "55.00"));

    @Override
    public List<Product> searchProducts(String keyword, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be positive");
        }
        String normalized = keyword == null ? "" : keyword.toLowerCase(Locale.ROOT);
        return CATALOG.stream()
                .filter(product -> normalized.isBlank()
                        || product.name().toLowerCase(Locale.ROOT).contains(normalized)
                        || product.category().toLowerCase(Locale.ROOT).contains(normalized))
                .limit(limit)
                .toList();
    }

    private static Product product(String externalId, String name, String category, String seller, String price) {
        return new Product(UUID.nameUUIDFromBytes(externalId.getBytes()), externalId, name, category, seller,
                new BigDecimal(price), "USD", URI.create("https://www.fiverr.com/" + seller),
                "Mock product for phase-one workflow validation.", ProductStatus.DISCOVERED, Instant.parse("2026-01-01T00:00:00Z"));
    }
}

