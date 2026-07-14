package com.aiaffiliate.domain.port;

import com.aiaffiliate.domain.model.AffiliateProduct;

import java.util.List;

/** Fiverr 产品数据入口；第一阶段由 Mock 实现提供数据。 */
public interface FiverrDataProvider {
    List<AffiliateProduct> searchProducts(String keyword, int limit);
}
