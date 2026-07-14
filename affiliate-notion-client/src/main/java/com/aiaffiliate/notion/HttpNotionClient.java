package com.aiaffiliate.notion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 基于 JDK HttpClient 的轻量 Notion API 实现。该类无 Spring 依赖，便于独立测试和复用。
 */
public final class HttpNotionClient implements NotionClient {

    private static final String DEFAULT_BASE_URL = "https://api.notion.com/v1/";
    private static final String NOTION_VERSION = "2022-06-28";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final URI baseUri;
    private final String token;

    public HttpNotionClient(String token, Duration timeout, ObjectMapper objectMapper) {
        this(token, timeout, objectMapper, URI.create(DEFAULT_BASE_URL));
    }

    public HttpNotionClient(String token, Duration timeout, ObjectMapper objectMapper, URI baseUri) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Notion token must not be blank");
        }
        Objects.requireNonNull(timeout, "timeout must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        this.baseUri = Objects.requireNonNull(baseUri, "baseUri must not be null");
        this.token = token;
        this.httpClient = HttpClient.newBuilder().connectTimeout(timeout).build();
    }

    @Override
    public JsonNode queryDatabase(String databaseId, NotionQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        Map<String, Object> body = new LinkedHashMap<>();
        if (!query.filter().isEmpty()) {
            body.put("filter", query.filter());
        }
        if (!query.sorts().isEmpty()) {
            body.put("sorts", query.sorts());
        }
        if (query.startCursor() != null && !query.startCursor().isBlank()) {
            body.put("start_cursor", query.startCursor());
        }
        if (query.pageSize() != null) {
            body.put("page_size", query.pageSize());
        }
        return send("POST", "databases/" + requireId(databaseId) + "/query", body);
    }

    @Override
    public JsonNode createPage(String databaseId, Map<String, Object> properties) {
        Map<String, Object> body = Map.of(
                "parent", Map.of("database_id", requireId(databaseId)),
                "properties", Map.copyOf(Objects.requireNonNull(properties, "properties must not be null")));
        return send("POST", "pages", body);
    }

    @Override
    public JsonNode updatePage(String pageId, Map<String, Object> properties) {
        Map<String, Object> body = Map.of(
                "properties", Map.copyOf(Objects.requireNonNull(properties, "properties must not be null")));
        return send("PATCH", "pages/" + requireId(pageId), body);
    }

    private JsonNode send(String method, String path, Object body) {
        try {
            HttpRequest request = HttpRequest.newBuilder(baseUri.resolve(path))
                    .header("Authorization", "Bearer " + token)
                    .header("Notion-Version", NOTION_VERSION)
                    .header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new NotionClientException(
                        "Notion API returned HTTP " + response.statusCode() + ": " + response.body());
            }
            return objectMapper.readTree(response.body());
        } catch (JsonProcessingException exception) {
            throw new NotionClientException("Unable to serialize Notion request", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new NotionClientException("Notion request was interrupted", exception);
        } catch (IOException exception) {
            throw new NotionClientException("Unable to call Notion API", exception);
        }
    }

    private static String requireId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Notion id must not be blank");
        }
        return value.trim();
    }
}
