package com.aiaffiliate.domain.model;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/** Fiverr 联盟产品聚合根。 */
public record AffiliateProduct(
        ProductId id, String productName, AffiliatePlatform platform, ProductType productType,
        String category, String subcategory, String fiverrKeyword, URI productUrl, URI affiliateUrl,
        String sellerName, SellerLevel sellerLevel, BigDecimal priceMin, BigDecimal priceMax,
        BigDecimal estimatedAov, BigDecimal rating, Integer reviewCount, Integer deliveryDays,
        Integer ordersInQueue, Set<String> targetAudiences, String primaryPainPoint,
        Integer visualPotential, Integer purchaseIntent, Integer commissionPotential,
        Integer productQualityScore, ProductStatus status, Set<String> dataSources,
        LocalDate lastCheckedDate, String notes, Instant createdAt, Instant updatedAt) {

    public AffiliateProduct {
        Objects.requireNonNull(id, "id must not be null");
        productName = DomainRules.text(productName, "productName");
        platform = platform == null ? AffiliatePlatform.UNKNOWN : platform;
        productType = productType == null ? ProductType.UNKNOWN : productType;
        category = category == null ? "" : category.trim();
        subcategory = subcategory == null ? "" : subcategory.trim();
        fiverrKeyword = fiverrKeyword == null ? "" : fiverrKeyword.trim();
        sellerName = sellerName == null ? "" : sellerName.trim();
        sellerLevel = sellerLevel == null ? SellerLevel.UNKNOWN : sellerLevel;
        requireNonNegative(priceMin, "priceMin"); requireNonNegative(priceMax, "priceMax"); requireNonNegative(estimatedAov, "estimatedAov");
        if (priceMin != null && priceMax != null && priceMax.compareTo(priceMin) < 0) throw new IllegalArgumentException("priceMax must not be less than priceMin");
        if (rating != null && (rating.signum() < 0 || rating.compareTo(new BigDecimal("5")) > 0)) throw new IllegalArgumentException("rating must be between 0 and 5");
        if (reviewCount != null && reviewCount < 0) throw new IllegalArgumentException("reviewCount must not be negative");
        if (deliveryDays != null && deliveryDays < 0) throw new IllegalArgumentException("deliveryDays must not be negative");
        if (ordersInQueue != null && ordersInQueue < 0) throw new IllegalArgumentException("ordersInQueue must not be negative");
        targetAudiences = DomainRules.immutableSet(targetAudiences);
        primaryPainPoint = primaryPainPoint == null ? "" : primaryPainPoint;
        DomainRules.optionalScore(visualPotential, "visualPotential");
        DomainRules.optionalScore(purchaseIntent, "purchaseIntent");
        DomainRules.optionalScore(commissionPotential, "commissionPotential");
        DomainRules.optionalScore(productQualityScore, "productQualityScore");
        status = status == null ? ProductStatus.DISCOVERED : status;
        dataSources = DomainRules.immutableSet(dataSources);
        notes = notes == null ? "" : notes;
        Objects.requireNonNull(createdAt, "createdAt must not be null"); Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    private static void requireNonNegative(BigDecimal value, String field) {
        if (value != null && value.signum() < 0) throw new IllegalArgumentException(field + " must not be negative");
    }
}
