package com.aiaffiliate.notion.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.net.URI;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.*;

/** Notion Property 的集中读写映射，业务 Service 不需要手写 JSON。 */
public class NotionPropertyMapper {
    public static final int RICH_TEXT_FRAGMENT_LIMIT = 2_000;
    private static final int PROPERTY_FRAGMENT_LIMIT = 100;

    public Map<String, Object> title(String value) { return Map.of("title", fragments(value, PROPERTY_FRAGMENT_LIMIT)); }
    public Map<String, Object> richText(String value) { return Map.of("rich_text", fragments(value, PROPERTY_FRAGMENT_LIMIT)); }
    public Map<String, Object> number(Number value) { return Collections.singletonMap("number", value); }
    public Map<String, Object> select(Enum<?> value) { return select(value == null ? null : value.name()); }
    public Map<String, Object> select(String value) { return Collections.singletonMap("select", value == null || value.isBlank() ? null : Map.of("name", value)); }
    public Map<String, Object> multiSelect(Collection<?> values) {
        if (values == null) return Map.of("multi_select", List.of());
        return Map.of("multi_select", values.stream().filter(Objects::nonNull).map(value -> Map.of("name", value instanceof Enum<?> e ? e.name() : value.toString())).toList());
    }
    public Map<String, Object> url(URI value) { return Collections.singletonMap("url", value == null ? null : value.toString()); }
    public Map<String, Object> date(Temporal value) { return Collections.singletonMap("date", value == null ? null : Map.of("start", value.toString())); }
    public Map<String, Object> relation(Collection<String> pageIds) { return Map.of("relation", pageIds == null ? List.of() : pageIds.stream().filter(Objects::nonNull).map(id -> Map.of("id", id)).toList()); }

    public List<Map<String, Object>> pageBodyBlocks(String content) {
        if (content == null || content.isBlank()) return List.of();
        List<Map<String, Object>> blocks = new ArrayList<>();
        for (String paragraph : content.split("\\R{2,}")) {
            for (String chunk : chunks(paragraph, RICH_TEXT_FRAGMENT_LIMIT)) {
                blocks.add(Map.of("object", "block", "type", "paragraph", "paragraph", Map.of("rich_text", fragments(chunk, 1))));
            }
        }
        return blocks;
    }

    public String text(JsonNode page, String property) {
        JsonNode node = page.path("properties").path(property); String type = node.path("type").asText();
        JsonNode values = node.path("title".equals(type) ? "title" : "rich_text"); StringBuilder out = new StringBuilder();
        values.forEach(value -> out.append(value.path("plain_text").asText(value.path("text").path("content").asText()))); return out.toString();
    }
    public Integer integer(JsonNode page, String property) { JsonNode node = page.path("properties").path(property).path("number"); return node.isNumber() ? node.intValue() : null; }
    public BigDecimal decimal(JsonNode page, String property) { JsonNode node = page.path("properties").path(property).path("number"); return node.isNumber() ? node.decimalValue() : null; }
    public String select(JsonNode page, String property) {
        JsonNode node = page.path("properties").path(property); String type = node.path("type").asText("select");
        return node.path(type).path("name").asText(null);
    }
    public Set<String> multiSelect(JsonNode page, String property) { Set<String> result = new LinkedHashSet<>(); page.path("properties").path(property).path("multi_select").forEach(value -> result.add(value.path("name").asText())); return result; }
    public URI uri(JsonNode page, String property) { String value = page.path("properties").path(property).path("url").asText(null); return value == null || value.isBlank() ? null : URI.create(value); }
    public LocalDate localDate(JsonNode page, String property) { String value = dateValue(page, property); return value == null ? null : LocalDate.parse(value.substring(0, 10)); }
    public OffsetDateTime offsetDateTime(JsonNode page, String property) { String value = dateValue(page, property); return value == null ? null : OffsetDateTime.parse(value); }
    public Set<String> relationIds(JsonNode page, String property) { Set<String> result = new LinkedHashSet<>(); page.path("properties").path(property).path("relation").forEach(value -> result.add(value.path("id").asText())); return result; }
    public Instant createdTime(JsonNode page) { String value = page.path("created_time").asText(); return value.isBlank() ? Instant.now() : Instant.parse(value); }
    public Instant updatedTime(JsonNode page) { String value = page.path("last_edited_time").asText(); return value.isBlank() ? createdTime(page) : Instant.parse(value); }
    public <E extends Enum<E>> E enumValue(String value, Class<E> type, E unknown) { if (value == null) return unknown; try { return Enum.valueOf(type, value); } catch (IllegalArgumentException ignored) { return unknown; } }

    private String dateValue(JsonNode page, String property) { return page.path("properties").path(property).path("date").path("start").asText(null); }
    private List<Map<String, Object>> fragments(String value, int maxFragments) {
        if (value == null || value.isEmpty()) return List.of();
        return chunks(value, RICH_TEXT_FRAGMENT_LIMIT).stream().limit(maxFragments)
                .map(chunk -> Map.<String, Object>of("type", "text", "text", Map.of("content", chunk))).toList();
    }
    private List<String> chunks(String value, int size) { List<String> result = new ArrayList<>(); for (int i = 0; i < value.length(); i += size) result.add(value.substring(i, Math.min(i + size, value.length()))); return result; }
}
