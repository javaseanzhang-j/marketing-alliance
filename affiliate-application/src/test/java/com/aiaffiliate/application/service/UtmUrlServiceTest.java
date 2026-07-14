package com.aiaffiliate.application.service;

import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtmUrlServiceTest {
    @Test void preservesExistingQueryAndEncodesValues() {
        URI result = new UtmUrlService().build(URI.create("https://example.com/page?ref=1"), "pin source", "organic", "夏季 campaign", null);
        assertEquals("https://example.com/page?ref=1&utm_source=pin%20source&utm_medium=organic&utm_campaign=%E5%A4%8F%E5%AD%A3%20campaign", result.toString());
    }
}
