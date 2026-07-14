package com.aiaffiliate.infrastructure.notion;

import com.aiaffiliate.application.reference.EntityReferenceRepository;
import com.aiaffiliate.notion.client.NotionClient;
import com.aiaffiliate.notion.config.NotionProperties;
import com.aiaffiliate.notion.mapper.NotionPropertyMapper;
import com.aiaffiliate.notion.registry.*;
import com.aiaffiliate.notion.schema.*;
import com.aiaffiliate.infrastructure.notion.mapper.*;
import com.aiaffiliate.infrastructure.notion.repository.*;
import com.aiaffiliate.domain.port.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Path;
import java.util.List;

/** Notion Schema、初始化器、Mapper 和 Repository Adapter 组合配置。 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(NotionClient.class)
public class NotionIntegrationConfiguration {
    @Bean NotionPropertyMapper notionPropertyMapper() { return new NotionPropertyMapper(); }
    @Bean NotionSchemaValidator notionSchemaValidator() { return new NotionSchemaValidator(); }
    @Bean NotionSchemaRequestMapper notionSchemaRequestMapper() { return new NotionSchemaRequestMapper(); }
    @Bean List<NotionDatabaseSchemaDefinition> notionSchemas() { return List.of(new KeywordsDatabaseSchema(), new AffiliateProductsDatabaseSchema(), new OpportunitiesDatabaseSchema(), new BridgePagesDatabaseSchema(), new PinterestContentDatabaseSchema()); }
    @Bean DatabaseIdRegistry databaseIdRegistry(NotionProperties properties, ObjectMapper mapper) { return new JsonDatabaseIdRegistry(Path.of(properties.initialization().registryPath()), mapper); }
    @Bean EntityReferenceRepository entityReferenceRepository() { return new InMemoryEntityReferenceRepository(); }
    @Bean NotionDatabaseLocator notionDatabaseLocator(NotionProperties properties, DatabaseIdRegistry registry) { return new NotionDatabaseLocator(properties, registry); }
    @Bean KeywordNotionMapper keywordNotionMapper(NotionPropertyMapper p, EntityReferenceRepository r) { return new KeywordNotionMapper(p,r); }
    @Bean AffiliateProductNotionMapper affiliateProductNotionMapper(NotionPropertyMapper p, EntityReferenceRepository r) { return new AffiliateProductNotionMapper(p,r); }
    @Bean OpportunityNotionMapper opportunityNotionMapper(NotionPropertyMapper p, EntityReferenceRepository r) { return new OpportunityNotionMapper(p,r); }
    @Bean BridgePageNotionMapper bridgePageNotionMapper(NotionPropertyMapper p, EntityReferenceRepository r) { return new BridgePageNotionMapper(p,r); }
    @Bean PinterestContentNotionMapper pinterestContentNotionMapper(NotionPropertyMapper p, EntityReferenceRepository r) { return new PinterestContentNotionMapper(p,r); }
    @Bean KeywordRepository keywordRepository(NotionClient c, KeywordNotionMapper m, EntityReferenceRepository r, NotionDatabaseLocator l) { return new NotionKeywordRepository(c,m,r,l); }
    @Bean AffiliateProductRepository affiliateProductRepository(NotionClient c, AffiliateProductNotionMapper m, EntityReferenceRepository r, NotionDatabaseLocator l) { return new NotionAffiliateProductRepository(c,m,r,l); }
    @Bean OpportunityRepository opportunityRepository(NotionClient c, OpportunityNotionMapper m, EntityReferenceRepository r, NotionDatabaseLocator l) { return new NotionOpportunityRepository(c,m,r,l); }
    @Bean BridgePageRepository bridgePageRepository(NotionClient c, BridgePageNotionMapper m, EntityReferenceRepository r, NotionDatabaseLocator l) { return new NotionBridgePageRepository(c,m,r,l); }
    @Bean PinterestContentRepository pinterestContentRepository(NotionClient c, PinterestContentNotionMapper m, EntityReferenceRepository r, NotionDatabaseLocator l) { return new NotionPinterestContentRepository(c,m,r,l); }
    @Bean NotionDatabaseInitializer notionDatabaseInitializer(NotionClient client, NotionProperties properties, DatabaseIdRegistry registry,
            List<NotionDatabaseSchemaDefinition> schemas, NotionSchemaValidator validator, NotionSchemaRequestMapper requestMapper) {
        return new NotionDatabaseInitializer(client, properties, registry, schemas, validator, requestMapper);
    }
}
