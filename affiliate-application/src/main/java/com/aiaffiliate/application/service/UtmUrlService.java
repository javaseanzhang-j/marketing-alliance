package com.aiaffiliate.application.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/** 在不覆盖原参数的前提下生成带 UTM 参数的最终 URL。 */
public class UtmUrlService {
    public URI build(URI destination, String source, String medium, String campaign, String content) {
        if (destination == null) throw new IllegalArgumentException("destination must not be null");
        Map<String, String> params = new LinkedHashMap<>();
        put(params, "utm_source", source); put(params, "utm_medium", medium);
        put(params, "utm_campaign", campaign); put(params, "utm_content", content);
        if (params.isEmpty()) return destination;
        StringBuilder value = new StringBuilder(destination.toString());
        value.append(destination.getRawQuery() == null ? '?' : '&');
        params.forEach((key, parameter) -> value.append(encode(key)).append('=').append(encode(parameter)).append('&'));
        value.setLength(value.length() - 1);
        return URI.create(value.toString());
    }
    private static void put(Map<String, String> values, String key, String value) { if (value != null && !value.isBlank()) values.put(key, value); }
    private static String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"); }
}
