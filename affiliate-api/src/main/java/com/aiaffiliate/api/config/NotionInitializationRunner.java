package com.aiaffiliate.api.config;

import com.aiaffiliate.application.notion.NotionAdministrationUseCase;
import com.aiaffiliate.notion.config.NotionProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/** 仅在配置开启或显式传入 --initialize-notion 时运行初始化器。 */
@Component
public class NotionInitializationRunner implements ApplicationRunner {
    private final NotionProperties properties; private final ObjectProvider<NotionAdministrationUseCase> useCase;
    public NotionInitializationRunner(NotionProperties properties,ObjectProvider<NotionAdministrationUseCase> useCase){this.properties=properties;this.useCase=useCase;}
    @Override public void run(ApplicationArguments args){boolean cli=args.containsOption("initialize-notion")||Arrays.asList(args.getSourceArgs()).contains("--initialize-notion");if(!properties.initialization().enabled()&&!cli)return;NotionAdministrationUseCase value=useCase.getIfAvailable();if(value==null)throw new IllegalStateException("Notion integration must be enabled before initialization");value.initialize();}
}
