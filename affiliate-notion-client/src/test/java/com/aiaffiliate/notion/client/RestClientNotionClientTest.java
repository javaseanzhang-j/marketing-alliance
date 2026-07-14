package com.aiaffiliate.notion.client;

import com.aiaffiliate.notion.dto.NotionQueryRequest;
import com.aiaffiliate.notion.exception.NotionAuthenticationException;
import com.aiaffiliate.notion.retry.NotionRetryPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

class RestClientNotionClientTest {
    private MockWebServer server;
    private RestClientNotionClient client;

    @BeforeEach
    void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        RestClient restClient = RestClient.builder()
                .baseUrl(server.url("/").toString())
                .defaultHeader("Authorization", "Bearer secret")
                .defaultHeader("Notion-Version", "2022-06-28")
                .build();
        client = new RestClientNotionClient(restClient, new ObjectMapper(), new NotionRetryPolicy(0));
    }

    @AfterEach
    void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    void queriesDatabaseAndMapsCursor() throws Exception {
        server.enqueue(json(200, """
                {"results":[{"id":"page-1"}],"has_more":true,"next_cursor":"cursor-2"}
                """));

        var result = client.queryDatabase("database-1", NotionQueryRequest.firstPage(25));

        assertEquals(1, result.results().size());
        assertTrue(result.hasMore());
        assertEquals("cursor-2", result.nextCursor());
        RecordedRequest request = server.takeRequest();
        assertEquals("/v1/databases/database-1/query", request.getPath());
        assertEquals("Bearer secret", request.getHeader("Authorization"));
        assertEquals("2022-06-28", request.getHeader("Notion-Version"));
        assertTrue(request.getBody().readUtf8().contains("\"page_size\":25"));
    }

    @Test
    void mapsAuthenticationFailureWithoutRetrying() {
        server.enqueue(json(401, "{\"message\":\"invalid token\"}"));

        NotionAuthenticationException error = assertThrows(NotionAuthenticationException.class,
                () -> client.getPage("page-1"));

        assertEquals("invalid token", error.getMessage());
        assertEquals(1, server.getRequestCount());
    }

    private static MockResponse json(int status, String body) {
        return new MockResponse().setResponseCode(status)
                .setHeader("Content-Type", "application/json")
                .setBody(body);
    }
}
