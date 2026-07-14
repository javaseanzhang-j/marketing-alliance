# Notion 数据库设计

Notion 是 MVP 的业务记录系统。五个数据库必须共享给同一个 `NOTION_TOKEN` 对应的 Integration。Java Domain Service 是评分真源，Notion 只保存结果并承担运营展示。

## 数据库与关系

```text
Keywords ↔ Affiliate Products ↔ Opportunities ↔ Bridge Pages ↔ Pinterest Content
```

初始化器创建以下双向关系：

- `Keywords.Products` ↔ `Affiliate Products.Keywords`
- `Keywords.Opportunities` ↔ `Opportunities.Primary Keyword`
- `Keywords.Bridge Pages` ↔ `Bridge Pages.Primary Keyword`
- `Keywords.Pinterest Content` ↔ `Pinterest Content.Primary Keyword`
- `Affiliate Products.Opportunities` ↔ `Opportunities.Product`
- `Affiliate Products.Bridge Pages` ↔ `Bridge Pages.Products`
- `Affiliate Products.Pinterest Content` ↔ `Pinterest Content.Product`
- `Opportunities.Bridge Pages` ↔ `Bridge Pages.Opportunity`
- `Opportunities.Pinterest Content` ↔ `Pinterest Content.Opportunity`
- `Bridge Pages.Pinterest Content` ↔ `Pinterest Content.Bridge Page`

`Opportunities.Secondary Keywords` 使用独立的单向 Relation，避免和 Primary Keyword 的镜像字段冲突。

## 字段概要

| Database | 标题/业务 ID | 主要业务字段 | 关系字段 |
| --- | --- | --- | --- |
| Keywords | `Keyword` / `Keyword ID` | 分类、意图、来源、语言、国家、搜索量、Pinterest Demand、商业意图、竞争、趋势、Keyword Score、Status、Notes | Products、Opportunities、Bridge Pages、Pinterest Content |
| Affiliate Products | `Product Name` / `Product ID` | 平台、产品类型、分类、Fiverr Keyword、产品/联盟 URL、卖家、价格、AOV、评分、评论、交付、受众、痛点、三项潜力、Product Quality Score、Status、数据源 | Keywords、Opportunities、Bridge Pages、Pinterest Content |
| Opportunities | `Opportunity Name` / `Opportunity ID` | Channel、Audience、Country、Funnel Stage、Content Angle、七项子分、Final Score、Priority、Recommendation、AI Summary、Evidence、Risks、Test Budget、Status | Product、Primary Keyword、Secondary Keywords、Bridge Pages、Pinterest Content |
| Bridge Pages | `Page Name` / `Bridge Page ID` | Page Type、Audience、Channel、Language、Slug、发布 URL、标题与 CTA、Outline、内容摘要、Disclosure、Trust Elements、AI Model、Prompt Version、Content Score、Compliance、Status、Published Date | Opportunity、Products、Primary Keyword、Pinterest Content |
| Pinterest Content | `Pin Name` / `Pin ID` | Pin Type、Creative Angle、标题、描述、Overlay、Image Prompt、模板、素材/目标 URL、UTM、Final URL、Board、Publishing Type、发布时间、发布 URL、Status、Compliance | Opportunity、Bridge Page、Product、Primary Keyword |

每个 Database 还包含 `Schema Version`、`Managed By`、Created Time 和 Last Edited Time（Bridge Pages 使用 Last Updated）。新建业务 Page 时，托管字段分别写入 `1.0` 与 `AI Affiliate Platform`。

## Schema 管理原则

- 优先通过本地 Registry 或环境变量定位 Database，不以名称作为唯一依据。
- 缺失字段和枚举项自动补充；额外字段与额外枚举项保留。
- 兼容字段保持不变；类型冲突或错误 Relation 只报告，不做破坏性覆盖。
- Status 优先创建为 Notion Status；API 不支持时降级为 Select，领域枚举保持不变。
- 第一阶段 Rollup/Formula 已有声明抽象，但复杂统计未启用。
- `Product Snapshots` 和 `Campaign Metrics` 仅预留 ID 配置。

## 长文本策略

Notion Rich Text 单 fragment 最长 2,000 字符。公共 Property Mapper 会自动拆分 fragment；`Bridge Pages.Full Content` 仅保存不超过约 1,900 字符的摘要，完整正文按段落拆成 Page Body Blocks，避免属性长度成为内容上限。

## 运维接口

- `GET /api/admin/notion/schema`：返回每个 Database 的缺失字段、类型冲突、缺失枚举、Relation 问题、警告和建议动作。
- `POST /api/admin/notion/initialize`：显式执行幂等初始化。

Notion 未启用时，管理接口返回 `503 Service Unavailable`，普通应用启动不受影响。
