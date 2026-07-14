package com.aiaffiliate.infrastructure.provider;

import com.aiaffiliate.domain.model.*;
import com.aiaffiliate.domain.port.FiverrDataProvider;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.net.URI;
import java.time.*;
import java.util.*;

/** 第一阶段 Fiverr 数据桩，真实爬虫不在本阶段范围内。 */
@Component
public class MockFiverrDataProvider implements FiverrDataProvider {
    private static final List<AffiliateProduct> CATALOG=List.of(
            product("fvr-001","Minimal logo design","Graphics & Design","northstudio","25.00"),
            product("fvr-002","WordPress speed optimization","Programming & Tech","speedcraft","40.00"),
            product("fvr-003","SEO blog content package","Writing & Translation","wordfoundry","55.00"));
    public List<AffiliateProduct>searchProducts(String keyword,int limit){if(limit<1)throw new IllegalArgumentException("limit must be positive");String q=keyword==null?"":keyword.toLowerCase(Locale.ROOT);return CATALOG.stream().filter(v->q.isBlank()||v.productName().toLowerCase(Locale.ROOT).contains(q)||v.category().toLowerCase(Locale.ROOT).contains(q)).limit(limit).toList();}
    private static AffiliateProduct product(String id,String name,String category,String seller,String price){Instant now=Instant.parse("2026-01-01T00:00:00Z");BigDecimal amount=new BigDecimal(price);return new AffiliateProduct(new ProductId(id),name,AffiliatePlatform.FIVERR,ProductType.SPECIFIC_GIG,category,"",name.toLowerCase(Locale.ROOT),URI.create("https://www.fiverr.com/"+seller),URI.create("https://www.fiverr.com/"+seller),seller,SellerLevel.LEVEL_2,amount,amount,amount,new BigDecimal("4.8"),250,3,2,Set.of("small business"),"Need trusted specialist",85,80,75,82,ProductStatus.DISCOVERED,Set.of("MOCK"),LocalDate.of(2026,1,1),"Phase-one mock product",now,now);}
}
