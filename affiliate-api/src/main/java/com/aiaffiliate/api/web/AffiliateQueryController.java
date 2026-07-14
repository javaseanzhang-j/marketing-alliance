package com.aiaffiliate.api.web;

import com.aiaffiliate.agent.KeywordAgent;
import com.aiaffiliate.domain.model.Keyword;
import com.aiaffiliate.domain.model.AffiliateProduct;
import com.aiaffiliate.domain.service.OpportunityScoringService;
import com.aiaffiliate.domain.port.FiverrDataProvider;
import com.aiaffiliate.domain.port.SEODataProvider;
import com.aiaffiliate.notion.client.NotionClient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/** 第一阶段只读查询与 Agent 试运行 API。 */
@RestController
@RequestMapping("/api/v1")
public class AffiliateQueryController {

    private final FiverrDataProvider fiverrDataProvider;
    private final SEODataProvider seoDataProvider;
    private final ObjectProvider<KeywordAgent> keywordAgentProvider;
    private final ObjectProvider<NotionClient> notionClientProvider;

    public AffiliateQueryController(
            FiverrDataProvider fiverrDataProvider,
            SEODataProvider seoDataProvider,
            ObjectProvider<KeywordAgent> keywordAgentProvider,
            ObjectProvider<NotionClient> notionClientProvider) {
        this.fiverrDataProvider = fiverrDataProvider;
        this.seoDataProvider = seoDataProvider;
        this.keywordAgentProvider = keywordAgentProvider;
        this.notionClientProvider = notionClientProvider;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        var leadingScore = new OpportunityScoringService().calculate(92, 88, 75, 90, 24, 84, 90);
        return new DashboardResponse(
                247, 84, 18, 42, leadingScore.score().value(),
                Map.of("ai", keywordAgentProvider.getIfAvailable() != null,
                        "notion", notionClientProvider.getIfAvailable() != null),
                Instant.now());
    }

    @GetMapping("/products")
    public List<AffiliateProduct> products(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
        return fiverrDataProvider.searchProducts(query, limit);
    }

    @GetMapping("/keywords")
    public List<Keyword> keywords(
            @RequestParam @NotBlank String seed,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
        return seoDataProvider.expandKeywords(seed, limit);
    }

    @PostMapping("/agents/keywords:discover")
    public AgentResponse discoverKeywords(@Valid @RequestBody KeywordDiscoveryRequest request) {
        KeywordAgent agent = keywordAgentProvider.getIfAvailable();
        if (agent == null) {
            throw new AgentUnavailableException();
        }
        return new AgentResponse(agent.discover(request.seedKeyword(), request.audience(), request.limit()));
    }

    public record DashboardResponse(
            long products,
            long keywords,
            long highPotentialOpportunities,
            long contentDrafts,
            int leadingScore,
            Map<String, Boolean> integrations,
            Instant generatedAt) {
    }

    public record KeywordDiscoveryRequest(@NotBlank String seedKeyword, @NotBlank String audience, @Min(1) @Max(30) int limit) {
    }

    public record AgentResponse(String content) {
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    static class AgentUnavailableException extends RuntimeException {
        AgentUnavailableException() {
            super("AI Agent is disabled. Set AFFILIATE_AI_ENABLED=true and OPENAI_API_KEY.");
        }
    }
}
