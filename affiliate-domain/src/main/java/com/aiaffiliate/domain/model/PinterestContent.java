package com.aiaffiliate.domain.model;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;

/** Pinterest 营销内容聚合根。 */
public record PinterestContent(
        PinterestContentId id, String pinName, OpportunityId opportunityId, BridgePageId bridgePageId,
        ProductId productId, KeywordId primaryKeywordId, PinType pinType, CreativeAngle creativeAngle,
        String pinTitle, String pinDescription, String overlayText, String imagePrompt,
        String designTemplate, URI assetUrl, URI destinationUrl, String utmSource, String utmMedium,
        String utmCampaign, String utmContent, URI finalUrl, String board, PublishingType publishingType,
        OffsetDateTime publishDate, URI publishedUrl, PinterestContentStatus status,
        ComplianceStatus complianceStatus, Instant createdAt, Instant updatedAt) {

    public PinterestContent {
        Objects.requireNonNull(id, "id must not be null"); pinName = DomainRules.text(pinName, "pinName");
        Objects.requireNonNull(opportunityId, "opportunityId must not be null"); Objects.requireNonNull(bridgePageId, "bridgePageId must not be null");
        Objects.requireNonNull(productId, "productId must not be null"); Objects.requireNonNull(primaryKeywordId, "primaryKeywordId must not be null");
        pinType = pinType == null ? PinType.UNKNOWN : pinType; creativeAngle = creativeAngle == null ? CreativeAngle.UNKNOWN : creativeAngle;
        pinTitle = empty(pinTitle); pinDescription = empty(pinDescription); overlayText = empty(overlayText); imagePrompt = empty(imagePrompt);
        designTemplate = empty(designTemplate); utmSource = empty(utmSource); utmMedium = empty(utmMedium); utmCampaign = empty(utmCampaign);
        utmContent = empty(utmContent); board = empty(board); publishingType = publishingType == null ? PublishingType.UNKNOWN : publishingType;
        status = status == null ? PinterestContentStatus.IDEA : status;
        complianceStatus = complianceStatus == null ? ComplianceStatus.NOT_REVIEWED : complianceStatus;
        Objects.requireNonNull(createdAt, "createdAt must not be null"); Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }
    private static String empty(String value) { return value == null ? "" : value; }
}
