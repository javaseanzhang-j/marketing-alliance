package com.aiaffiliate.domain.port;

import com.aiaffiliate.domain.model.*;
import java.util.*;

/** 联盟产品持久化端口。 */
public interface AffiliateProductRepository {
    AffiliateProduct save(AffiliateProduct value); AffiliateProduct update(AffiliateProduct value); Optional<AffiliateProduct> findById(ProductId id);
    List<AffiliateProduct> findAll(); List<AffiliateProduct> findByStatus(ProductStatus status); PageResult<AffiliateProduct> findPage(PageRequest page);
    List<AffiliateProduct> findByPlatformAndCategory(AffiliatePlatform platform, String category); void archive(ProductId id);
}
