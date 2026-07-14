package com.aiaffiliate.api.web;

import com.aiaffiliate.application.notion.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

/** Notion 初始化和 Schema 状态管理接口。Controller 不直接调用 NotionClient。 */
@RestController
@RequestMapping("/api/admin/notion")
public class NotionAdminController {
    private final ObjectProvider<NotionAdministrationUseCase> useCase;
    public NotionAdminController(ObjectProvider<NotionAdministrationUseCase> useCase){this.useCase=useCase;}
    @GetMapping("/schema") public List<SchemaStatus> schemas(){return required().validateSchemas();}
    @PostMapping("/initialize") public InitializationResult initialize(){return required().initialize();}
    private NotionAdministrationUseCase required(){NotionAdministrationUseCase value=useCase.getIfAvailable();if(value==null)throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Notion integration is disabled");return value;}
}
