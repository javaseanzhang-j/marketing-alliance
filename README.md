# AI 联盟营销智能平台

这是一个面向 AI 辅助联盟营销研究和内容生产流程的企业级项目基础框架。第一阶段聚焦 Fiverr 联盟产品，支持分析联盟机会、生成 Bridge Page 方案以及制作 Pinterest 营销内容。Notion 作为规划中的业务数据存储；按照第一阶段要求，目前各外部数据提供器均使用可预测的 Mock 实现。

## 已实现内容

- 基于 Java 21 和 Spring Boot 3.5 的 REST API
- 遵循 DDD 思想的领域模型，包含经过校验的不可变实体和值对象
- LangChain4j `ChatModel` 配置、提示词模板及四个类型安全的 Agent Service
- 支持查询数据库、创建页面和更新页面的 Notion HTTP Client
- Fiverr、Pinterest 和 SEO 数据提供器接口及 Mock 实现
- 基于 Next.js、TypeScript 和 Tailwind CSS 的响应式智能分析工作台
- 默认无需任何密钥即可启动；AI 和 Notion 集成可按需启用

## 项目结构

```text
ai-affiliate-platform
├── affiliate-api             # Spring Boot 启动入口和 REST API
├── affiliate-domain          # 领域模型和数据提供器端口
├── affiliate-infrastructure  # Mock 数据提供器适配器
├── affiliate-ai-agent        # LangChain4j Agent、提示词和 ChatModel 配置
├── affiliate-notion-client   # Notion API 边界和 JDK HTTP 实现
└── affiliate-web             # Next.js 智能分析工作台
```

模块依赖统一指向领域层：基础设施模块和 AI 模块依赖领域模块，API 模块负责组合各个模块。领域模块不依赖 Spring、Notion 或 LangChain4j。

## 环境要求

- JDK 21
- Maven 3.8+
- Node.js 22.13+ 和 npm

## 启动后端

```bash
cp .env.example .env
mvn clean verify
mvn -pl affiliate-api -am spring-boot:run
```

API 默认运行在 `http://localhost:8080`。常用接口如下：

- `GET /actuator/health`
- `GET /api/v1/dashboard`
- `GET /api/v1/products?query=seo&limit=20`
- `GET /api/v1/keywords?seed=wordpress&limit=20`
- `POST /api/v1/agents/keywords:discover`

AI 和 Notion 默认关闭，因此应用无需配置密钥即可启动。如需启用集成，请根据 `.env.example` 配置环境变量，并将对应的 `*_ENABLED` 开关设置为 `true`。

## 启动 Web 工作台

```bash
cd affiliate-web
npm install
npm run dev
```

需要时，也可以让 Web 模块参与 Maven 构建生命周期：

```bash
mvn -Pweb-build verify
```

## AI Agent 框架

`AiAgentConfiguration` 负责创建一个 OpenAI `ChatModel` 和四个 LangChain4j AI Service：

- `KeywordAgent`：发现并分析联盟营销关键词
- `ProductAnalyzerAgent`：分析产品价值和推广潜力
- `BridgePageGeneratorAgent`：生成 Bridge Page 内容方案
- `PinterestContentAgent`：生成 Pinterest 营销内容

所有提示词模板统一维护在 `PromptTemplates` 中，并要求 Agent 仅使用调用方提供的事实、明确标注假设，同时生成符合联盟营销规范的内容。只有在 `AFFILIATE_AI_ENABLED=true` 时才会创建 Agent Bean，从而避免测试或本地开发期间意外产生付费 API 调用。

## Notion 配置

创建五个 Notion Database，将它们共享给同一个 Notion Integration，然后配置对应的 Database ID。字段级数据库结构详见 [docs/notion-databases.md](docs/notion-databases.md)。

`HttpNotionClient` 提供以下基础操作：

- 查询 Database
- 创建 Page
- 更新 Page

Notion 类型不会进入领域层，从而保持领域模型与存储技术解耦。

## 第一阶段边界

第一阶段不包含真实爬虫或非官方数据抓取。Mock 数据提供器保证整体架构可以运行、测试和替换。

后续迭代建议依次实现：

1. 应用层业务用例和 Notion Repository Adapter
2. 联盟机会评分编排流程
3. Bridge Page 与 Pinterest 内容审核命令
4. Fiverr、SEO 和 Pinterest 官方数据源适配器
5. 幂等数据同步与任务调度机制
