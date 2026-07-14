package com.aiaffiliate.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.aiaffiliate.notion.config.NotionProperties;

/** AI Affiliate Intelligence Platform 后端启动入口。 */
@SpringBootApplication(scanBasePackages = "com.aiaffiliate")
@EnableConfigurationProperties(NotionProperties.class)
public class AffiliateApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AffiliateApiApplication.class, args);
    }
}
