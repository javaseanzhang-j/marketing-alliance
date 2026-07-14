package com.aiaffiliate.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/** 生成 Bridge Page 信息架构与合规文案方案的 AI Service。 */
public interface BridgePageGeneratorAgent {

    @SystemMessage(PromptTemplates.AFFILIATE_SYSTEM)
    @UserMessage(PromptTemplates.BRIDGE_PAGE)
    String generate(
            @V("productName") String productName,
            @V("keyword") String keyword,
            @V("audience") String audience);
}

