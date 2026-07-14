package com.aiaffiliate.notion.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotionSchemaValidatorTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void reportsMissingOptionsAndAcceptsStatusSelectFallback() throws Exception {
        var actual = mapper.readTree("""
                {"properties":{
                  "Keyword":{"type":"title","title":{}},
                  "Intent":{"type":"select","select":{"options":[{"name":"COMMERCIAL"}]}},
                  "Status":{"type":"select","select":{"options":[{"name":"INBOX"}]}}
                }}
                """);

        var report = new NotionSchemaValidator().validate(new KeywordsDatabaseSchema(), actual);

        assertFalse(report.valid());
        assertTrue(report.missingSelectOptions().contains("Intent:INFORMATIONAL"));
        assertTrue(report.warnings().stream().anyMatch(value -> value.contains("Select 兼容 Status")));
        assertFalse(report.incompatibleProperties().stream().anyMatch(value -> value.startsWith("Status")));
    }

    @Test
    void reportsRelationThatTargetsWrongDatabase() throws Exception {
        var actual = mapper.readTree("""
                {"properties":{"Products":{"type":"relation","relation":{"database_id":"wrong-id"}}}}
                """);

        var report = new NotionSchemaValidator().validate(
                new KeywordsDatabaseSchema(), actual, java.util.Map.of("affiliate-products", "expected-id"));

        assertTrue(report.invalidRelations().stream().anyMatch(value -> value.startsWith("Products 指向错误")));
    }
}
