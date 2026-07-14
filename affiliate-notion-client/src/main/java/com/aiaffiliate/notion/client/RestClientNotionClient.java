package com.aiaffiliate.notion.client;

import com.aiaffiliate.notion.dto.*;
import com.aiaffiliate.notion.exception.*;
import com.aiaffiliate.notion.retry.NotionRetryPolicy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** 基于 Spring RestClient 的 Notion REST API 实现。 */
public class RestClientNotionClient implements NotionClient {
    private final RestClient client; private final ObjectMapper mapper; private final NotionRetryPolicy retryPolicy;
    public RestClientNotionClient(RestClient client, ObjectMapper mapper, NotionRetryPolicy retryPolicy) {
        this.client = Objects.requireNonNull(client); this.mapper = Objects.requireNonNull(mapper); this.retryPolicy = Objects.requireNonNull(retryPolicy);
    }
    @Override public JsonNode createDatabase(NotionDatabaseRequest request) { return execute(HttpMethod.POST, "/v1/databases", request); }
    @Override public JsonNode getDatabaseSchema(String databaseId) { return execute(HttpMethod.GET, "/v1/databases/" + id(databaseId), null); }
    @Override public JsonNode updateDatabaseSchema(String databaseId, Map<String, Object> properties) { return execute(HttpMethod.PATCH, "/v1/databases/" + id(databaseId), Map.of("properties", properties)); }
    @Override public NotionPageResult queryDatabase(String databaseId, NotionQueryRequest request) {
        JsonNode node = execute(HttpMethod.POST, "/v1/databases/" + id(databaseId) + "/query", request);
        List<JsonNode> results = new ArrayList<>(); node.path("results").forEach(results::add);
        return new NotionPageResult(results, node.path("has_more").asBoolean(false), node.path("next_cursor").isNull() ? null : node.path("next_cursor").asText(null));
    }
    @Override public JsonNode createPage(NotionPageRequest request) { return execute(HttpMethod.POST, "/v1/pages", request); }
    @Override public JsonNode getPage(String pageId) { return execute(HttpMethod.GET, "/v1/pages/" + id(pageId), null); }
    @Override public JsonNode updatePageProperties(String pageId, Map<String, Object> properties) { return execute(HttpMethod.PATCH, "/v1/pages/" + id(pageId), Map.of("properties", properties)); }
    @Override public JsonNode archivePage(String pageId) { return execute(HttpMethod.PATCH, "/v1/pages/" + id(pageId), Map.of("archived", true)); }
    @Override public JsonNode appendPageBlocks(String pageId, List<Map<String, Object>> children) { return execute(HttpMethod.PATCH, "/v1/blocks/" + id(pageId) + "/children", Map.of("children", children)); }

    private JsonNode execute(HttpMethod method, String uri, Object body) {
        RuntimeException last = null;
        for (int attempt = 0; attempt < retryPolicy.maxAttempts(); attempt++) {
            try { return exchange(method, uri, body); }
            catch (RuntimeException failure) {
                last = failure;
                if (!retryable(failure) || attempt + 1 >= retryPolicy.maxAttempts()) throw failure;
                sleep(retryPolicy.delay(attempt, failure));
            }
        }
        throw last == null ? new NotionIntegrationException("Notion request failed", 500) : last;
    }

    private JsonNode exchange(HttpMethod method, String uri, Object body) {
        RestClient.RequestBodySpec request = client.method(method).uri(uri);
        if (body != null) request.body(body);
        return request.exchange((req, response) -> {
            int status = response.getStatusCode().value();
            String responseBody;
            try { responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8); }
            catch (IOException e) { throw new NotionIntegrationException("Unable to read Notion response", status, e); }
            if (status >= 200 && status < 300) {
                try { return responseBody.isBlank() ? mapper.createObjectNode() : mapper.readTree(responseBody); }
                catch (IOException e) { throw new NotionIntegrationException("Invalid JSON returned by Notion", status, e); }
            }
            String message = safeMessage(responseBody, status);
            throw map(status, message, response.getHeaders().getFirst("Retry-After"));
        });
    }

    private String safeMessage(String body, int status) {
        try { String message = mapper.readTree(body).path("message").asText(); return message.isBlank() ? "Notion API returned HTTP " + status : message; }
        catch (Exception ignored) { return "Notion API returned HTTP " + status; }
    }
    private RuntimeException map(int status, String message, String retryAfter) {
        return switch (status) {
            case 400 -> new NotionValidationException(message); case 401 -> new NotionAuthenticationException(message);
            case 403 -> new NotionPermissionException(message); case 404 -> new NotionResourceNotFoundException(message);
            case 409 -> new NotionConflictException(message); case 429 -> new NotionRateLimitException(message, parseRetryAfter(retryAfter));
            default -> new NotionIntegrationException(message, status);
        };
    }
    private static boolean retryable(RuntimeException error) { return error instanceof NotionRateLimitException || error instanceof NotionIntegrationException e && e.statusCode() >= 500; }
    private static long parseRetryAfter(String value) { try { return value == null ? 1 : Long.parseLong(value); } catch (NumberFormatException e) { return 1; } }
    private static void sleep(java.time.Duration duration) { try { Thread.sleep(duration); } catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new NotionIntegrationException("Notion retry interrupted", 500, e); } }
    private static String id(String value) { if (value == null || value.isBlank()) throw new IllegalArgumentException("Notion id must not be blank"); return value.trim(); }
}
