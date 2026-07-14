package com.aiaffiliate.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/** 发现并解释高意图联盟关键词的 AI Service。 */
public interface KeywordAgent {

    @SystemMessage(PromptTemplates.AFFILIATE_SYSTEM)
    @UserMessage(PromptTemplates.KEYWORD_DISCOVERY)
    String discover(
            @V("seedKeyword") String seedKeyword,
            @V("audience") String audience,
            @V("limit") int limit);
}

