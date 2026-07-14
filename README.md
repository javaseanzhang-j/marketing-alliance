# AI Affiliate Intelligence Platform

Enterprise project foundation for an AI-assisted affiliate research and content workflow. Phase one targets Fiverr offers, calculates opportunity signals, prepares Bridge Page plans and produces Pinterest content. Notion is the planned business system of record; provider integrations are currently deterministic mocks, as requested.

## What is included

- Java 21 and Spring Boot 3.5 REST API
- DDD-oriented domain model with validated immutable entities and value objects
- LangChain4j ChatModel configuration, prompt templates and four typed Agent Services
- Notion HTTP client for database queries and page create/update operations
- Mock implementations of Fiverr, Pinterest and SEO provider ports
- Responsive Next.js/TypeScript/Tailwind intelligence workspace
- Credential-free startup by default; AI and Notion integrations are opt-in

## Project structure

```text
ai-affiliate-platform
├── affiliate-api             # Spring Boot entry point and REST endpoints
├── affiliate-domain          # Domain model and provider ports
├── affiliate-infrastructure  # Mock provider adapters
├── affiliate-ai-agent        # LangChain4j agents, prompts and ChatModel config
├── affiliate-notion-client   # Notion API boundary and JDK HTTP implementation
└── affiliate-web             # Next.js intelligence workspace
```

Dependencies point inward: infrastructure and AI depend on the domain; the API composes the modules. The domain has no Spring, Notion or LangChain4j dependency.

## Prerequisites

- JDK 21
- Maven 3.8+
- Node.js 22.13+ and npm

## Start the backend

```bash
cp .env.example .env
mvn clean verify
mvn -pl affiliate-api -am spring-boot:run
```

The API starts on `http://localhost:8080`. Useful endpoints:

- `GET /actuator/health`
- `GET /api/v1/dashboard`
- `GET /api/v1/products?query=seo&limit=20`
- `GET /api/v1/keywords?seed=wordpress&limit=20`
- `POST /api/v1/agents/keywords:discover`

AI and Notion are disabled by default, so the application starts without secrets. Export the variables from `.env.example` and set the corresponding `*_ENABLED` flag to `true` to activate an integration.

## Start the web workspace

```bash
cd affiliate-web
npm install
npm run dev
```

The Web module can also participate in the Maven lifecycle when explicitly requested:

```bash
mvn -Pweb-build verify
```

## Agent framework

`AiAgentConfiguration` creates one OpenAI `ChatModel` and four LangChain4j AI Services:

- `KeywordAgent`
- `ProductAnalyzerAgent`
- `BridgePageGeneratorAgent`
- `PinterestContentAgent`

Prompt templates live in `PromptTemplates` and require supplied facts, visible assumptions and compliant affiliate copy. Agent beans only exist when `AFFILIATE_AI_ENABLED=true`, preventing accidental paid calls in tests or local development.

## Notion setup

Create five databases, share them with one Notion integration and configure their IDs. The property-level schema is documented in [docs/notion-databases.md](docs/notion-databases.md). `HttpNotionClient` exposes query database, create page and update page operations without leaking Notion types into the domain.

## Phase-one boundaries

No crawler or unofficial scraping is included. Mock providers make the architecture runnable and replaceable. The next increment should add application use cases and Notion repository adapters, followed by opportunity scoring orchestration, content review commands and idempotent synchronization.
