package com.aiaffiliate.infrastructure.notion.repository;

import com.aiaffiliate.application.reference.*;
import com.aiaffiliate.domain.port.*;
import com.aiaffiliate.infrastructure.notion.mapper.NotionEntityMapper;
import com.aiaffiliate.notion.client.NotionClient;
import com.aiaffiliate.notion.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.*;

/** Notion Repository Adapter 的分页、Page ID 映射和 CRUD 公共实现。 */
abstract class AbstractNotionRepository<T> {
    protected final NotionClient client; protected final NotionEntityMapper<T> mapper; protected final EntityReferenceRepository references;
    private final NotionDatabaseLocator locator; private final String logicalName; private final String idProperty;
    protected AbstractNotionRepository(NotionClient client,NotionEntityMapper<T> mapper,EntityReferenceRepository refs,NotionDatabaseLocator locator,String logicalName,String idProperty){this.client=client;this.mapper=mapper;this.references=refs;this.locator=locator;this.logicalName=logicalName;this.idProperty=idProperty;}
    protected T saveEntity(T entity){JsonNode page=client.createPage(NotionPageRequest.inDatabase(databaseId(),mapper.toCreateProperties(entity),mapper.toPageBody(entity)));String pageId=page.path("id").asText();if(pageId.isBlank())throw new IllegalStateException("Notion did not return a page id");Instant now=Instant.now();references.save(new NotionEntityReference(mapper.domainId(entity),pageId,mapper.entityType(),now,now));return mapper.fromPage(page);}
    protected T updateEntity(T entity){String pageId=reference(entity).notionPageId();JsonNode page=client.updatePageProperties(pageId,mapper.toUpdateProperties(entity));return mapper.fromPage(page);}
    protected Optional<T> findEntity(String domainId){Optional<NotionEntityReference> ref=references.findByDomainId(domainId,mapper.entityType());if(ref.isPresent())return Optional.of(mapper.fromPage(client.getPage(ref.get().notionPageId())));NotionPageResult result=client.queryDatabase(databaseId(),new NotionQueryRequest(Map.of("property",idProperty,"rich_text",Map.of("equals",domainId)),List.of(),null,1));if(result.results().isEmpty())return Optional.empty();JsonNode page=result.results().getFirst();Instant now=Instant.now();references.save(new NotionEntityReference(domainId,page.path("id").asText(),mapper.entityType(),now,now));return Optional.of(mapper.fromPage(page));}
    protected List<T> findAllEntities(){List<T> result=new ArrayList<>();String cursor=null;do{NotionPageResult page=client.queryDatabase(databaseId(),new NotionQueryRequest(Map.of(),List.of(),cursor,100));page.results().stream().map(mapper::fromPage).forEach(result::add);cursor=page.hasMore()?page.nextCursor():null;}while(cursor!=null);return result;}
    protected PageResult<T> page(PageRequest request){String cursor=null;NotionPageResult result=null;for(int i=0;i<=request.page();i++){result=client.queryDatabase(databaseId(),new NotionQueryRequest(Map.of(),List.of(),cursor,request.size()));if(i<request.page()&&!result.hasMore())return new PageResult<>(List.of(),request.page(),request.size(),false,null);cursor=result.nextCursor();}return new PageResult<>(result.results().stream().map(mapper::fromPage).toList(),request.page(),request.size(),result.hasMore(),result.nextCursor());}
    protected void archiveEntity(String domainId){findEntity(domainId).orElseThrow(()->new NoSuchElementException("Entity not found: "+domainId));client.archivePage(references.findByDomainId(domainId,mapper.entityType()).orElseThrow().notionPageId());}
    private NotionEntityReference reference(T entity){return references.findByDomainId(mapper.domainId(entity),mapper.entityType()).orElseThrow(()->new NoSuchElementException("Entity reference not found: "+mapper.domainId(entity)));}
    protected String databaseId(){return locator.require(logicalName);}
}
