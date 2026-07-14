package com.aiaffiliate.agent;

/**
 * 集中维护 Agent 提示词。常量可直接用于 LangChain4j 注解，并便于后续版本化和 A/B 测试。
 */
public final class PromptTemplates {

    public static final String AFFILIATE_SYSTEM = """
            You are a senior affiliate marketing strategist specializing in Fiverr offers.
            Use only the facts supplied by the caller. Clearly label assumptions.
            Optimize for helpfulness, audience trust and policy-compliant conversion.
            Return concise, structured markdown in the language used by the caller.
            """;

    public static final String KEYWORD_DISCOVERY = """
            Expand the seed keyword {{seedKeyword}} for audience {{audience}}.
            Produce {{limit}} long-tail keywords. For each include search intent,
            funnel stage, content angle and a one-sentence rationale.
            """;

    public static final String PRODUCT_ANALYSIS = """
            Analyze this Fiverr product for affiliate promotion:
            {{productContext}}
            Evaluate buyer pain, differentiation, trust signals, objections,
            content fit and commission potential. End with a 0-100 recommendation.
            """;

    public static final String BRIDGE_PAGE = """
            Create a bridge page plan for product {{productName}} and keyword {{keyword}}.
            Audience: {{audience}}. Include headline, promise, section outline,
            proof requirements, objections, disclosure placement and CTA.
            Do not invent product claims.
            """;

    public static final String PINTEREST_CONTENT = """
            Create {{count}} Pinterest content concepts for {{productName}} using keyword {{keyword}}.
            Each concept needs a pin title, description, visual direction, overlay copy,
            CTA and 3-5 relevant hashtags. Avoid spam and unsupported claims.
            """;

    private PromptTemplates() {
    }
}

