# AI 联盟营销智能平台

AI Affiliate Intelligence Platform 是一个面向联盟营销选品、机会评估、桥接页生产与 Pinterest 内容运营的 Java 21 平台。MVP 使用 Notion 作为业务数据存储和人工运营后台，主链路为：

```text
种子关键词 → 联盟产品 → 机会分析 → 桥接页 → Pinterest 内容
```

## 已实现能力

- 完整领域模型与强类型 ID，领域层不依赖 Spring、HTTP 或 Notion
- 关键词、产品质量、机会优先级三套领域评分服务，统一使用 `0–100` 的 `Score` 值对象
- 五类领域 Repository Port，以及基于 Notion 的 CRUD、归档、分页和业务条件查询适配器
- Spring `RestClient` 实现的 Notion API 客户端，支持 Database、Page、分页查询、Block、Relation、Rollup 和 Formula
- 声明式定义五个 Notion Database，并按“基础字段 → Relation → Rollup → Formula”顺序幂等初始化
- Schema 差异检测：补充缺失字段和枚举项，保留用户字段，类型冲突不做破坏性修改
- Status 创建失败时自动降级为 Select
- 领域 ID 与 Notion Page ID 独立映射，避免基础设施标识进入领域模型
- Rich Text 自动按 2,000 字符拆分，桥接页完整正文写入 Page Body Blocks
- Pinterest Final URL 在 Java 应用层生成，保留已有参数并对 UTM 参数编码
- 统一异常映射、429 `Retry-After`、5xx 指数退避、随机抖动和有限重试
- Notion 管理接口和显式初始化命令；默认启动不会创建数据库
- Fiverr、Pinterest、SEO Mock 数据提供器与 LangChain4j Agent 基础能力
- Next.js 智能分析工作台

## 模块结构

```text
ai-affiliate-platform
├── affiliate-domain          # 领域模型、评分规则和 Repository Port
├── affiliate-application     # 应用服务、UTM、Notion 管理用例和实体引用抽象
├── affiliate-notion-client   # Notion RestClient、DTO、Schema、重试和 Registry
├── affiliate-infrastructure  # Notion Mapper、Repository Adapter、初始化器和数据提供器
├── affiliate-ai-agent        # LangChain4j Agent 与提示词
├── affiliate-api             # Spring Boot 启动入口和 REST API
└── affiliate-web             # Next.js 工作台
```

依赖方向遵循六边形架构：Domain 位于中心，Application 编排用例，Notion Client 与 Infrastructure 实现外部适配，API 只调用应用或领域端口。

## 环境要求

- JDK 21
- Maven 3.8+
- Node.js 22.13+（仅 Web）

## 构建与启动

```bash
cp .env.example .env
mvn clean verify
mvn -pl affiliate-api -am spring-boot:run
```

后端默认运行在 `http://localhost:8080`。Notion 和 AI 默认关闭，因此本地构建不需要任何外部密钥，也不会产生外部请求。

常用接口：

- `GET /actuator/health`
- `GET /api/v1/dashboard`
- `GET /api/v1/products?query=seo&limit=20`
- `GET /api/v1/keywords?seed=wordpress&limit=20`
- `GET /api/admin/notion/schema`
- `POST /api/admin/notion/initialize`

## Notion 配置与初始化

先创建 Notion Integration，把目标父页面共享给该 Integration，然后设置：

```bash
export NOTION_ENABLED=true
export NOTION_TOKEN='secret_xxx'
export NOTION_PARENT_PAGE_ID='xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'
export NOTION_API_VERSION='2022-06-28'
```

首次初始化可任选一种方式：

```bash
# 仅本次启动执行
mvn -pl affiliate-api -am spring-boot:run \
  -Dspring-boot.run.arguments=--initialize-notion

# 或通过配置开启
export NOTION_INITIALIZATION_ENABLED=true
mvn -pl affiliate-api -am spring-boot:run
```

初始化器会按顺序创建 `Keywords`、`Affiliate Products`、`Opportunities`、`Bridge Pages`、`Pinterest Content`，并把生成的 ID 写入 `.notion-databases.json`。该文件已加入 `.gitignore`，程序不会修改 `.env`。已有 Database ID 也可通过 `NOTION_*_DATABASE_ID` 环境变量提供。

初始化结束后应将 `NOTION_INITIALIZATION_ENABLED` 恢复为 `false`。重复执行是幂等的，但默认关闭能避免每次启动都访问远程 Schema。详细字段与关系见 [Notion 数据库设计](docs/notion-databases.md)。

## Web 工作台

```bash
cd affiliate-web
npm install
npm run dev
```

如需让 Web 参与 Maven 生命周期：

```bash
mvn -Pweb-build verify
```

## 当前边界

第一阶段不包含真实爬虫或非官方数据抓取。Fiverr、Pinterest 和 SEO 数据源仍是可替换的 Mock Adapter；`Product Snapshots` 与 `Campaign Metrics` 已预留配置，但不在本阶段自动创建。
