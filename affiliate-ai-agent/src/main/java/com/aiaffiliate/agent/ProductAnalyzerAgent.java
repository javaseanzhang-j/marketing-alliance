package com.aiaffiliate.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/** 分析产品受众价值、风险与转化潜力的 AI Service。 */
public interface ProductAnalyzerAgent {

    @SystemMessage(PromptTemplates.AFFILIATE_SYSTEM)
    @UserMessage(PromptTemplates.PRODUCT_ANALYSIS)
    String analyze(@V("productContext") String productContext);
}

