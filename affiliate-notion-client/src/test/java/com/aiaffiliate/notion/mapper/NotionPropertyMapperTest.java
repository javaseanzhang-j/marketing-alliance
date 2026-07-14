package com.aiaffiliate.notion.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotionPropertyMapperTest {
    private final NotionPropertyMapper mapper = new NotionPropertyMapper();

    @Test
    @SuppressWarnings("unchecked")
    void splitsRichTextAtNotionLimit() {
        String text = "a".repeat(4_001);
        var property = mapper.richText(text);
        var fragments = (java.util.List<java.util.Map<String, Object>>) property.get("rich_text");

        assertEquals(3, fragments.size());
        assertEquals(2_000, content(fragments.get(0)).length());
        assertEquals(1, content(fragments.get(2)).length());
    }

    @Test
    void createsPageBlocksForLongContent() {
        var blocks = mapper.pageBodyBlocks("a".repeat(2_001) + "\n\nsecond paragraph");
        assertEquals(3, blocks.size());
        assertEquals("paragraph", blocks.getFirst().get("type"));
    }

    @SuppressWarnings("unchecked")
    private static String content(java.util.Map<String, Object> fragment) {
        return (String) ((java.util.Map<String, Object>) fragment.get("text")).get("content");
    }
}
