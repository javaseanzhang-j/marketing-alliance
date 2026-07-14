package com.aiaffiliate.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/** 生成 Pinterest 标题、描述和视觉方向的 AI Service。 */
public interface PinterestContentAgent {

    @SystemMessage(PromptTemplates.AFFILIATE_SYSTEM)
    @UserMessage(PromptTemplates.PINTEREST_CONTENT)
    String generate(
            @V("productName") String productName,
            @V("keyword") String keyword,
            @V("count") int count);
}

