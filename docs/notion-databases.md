# Notion database design

Notion is the phase-one system of record. Each database should grant access to the same internal integration used by `NOTION_TOKEN`. Relation fields keep records connected without duplicating full objects.

## Affiliate Product Database

| Property | Notion type | Purpose |
| --- | --- | --- |
| Name | Title | Product/service name |
| External ID | Rich text | Provider identifier; business uniqueness key |
| Provider | Select | `Fiverr` in phase one |
| Category | Select | Fiverr category |
| Seller | Rich text | Seller display name |
| Starting Price | Number | Entry price |
| Currency | Select | ISO currency code |
| Affiliate URL | URL | Tracked destination |
| Summary | Rich text | Source or analyst summary |
| Status | Status | Discovered → Analyzed → Shortlisted → Archived |
| Discovered At | Date | First ingestion time |

## Keyword Database

| Property | Notion type | Purpose |
| --- | --- | --- |
| Phrase | Title | Normalized keyword |
| Intent | Select | Informational, Commercial, Transactional |
| Search Volume | Number | Estimated monthly demand |
| Difficulty | Number | 0–100 competition estimate |
| Source | Select | SEO provider or manual |
| Status | Status | Candidate → Approved → Paused → Archived |
| Products | Relation | Candidate product matches |
| Created At | Date | Record creation time |

## Opportunity Analysis Database

| Property | Notion type | Purpose |
| --- | --- | --- |
| Name | Title | Human-readable analysis title |
| Product | Relation | Affiliate Product Database |
| Keyword | Relation | Keyword Database |
| Demand | Number | 0–100 component score |
| Competition Advantage | Number | 0–100 component score |
| Commission Potential | Number | 0–100 component score |
| Content Fit | Number | 0–100 component score |
| Total Score | Formula | Weighted final score |
| AI Rationale | Rich text | Evidence and assumptions |
| Status | Status | New → Reviewed → Approved → Rejected |
| Analyzed At | Date | Analysis timestamp |

## Bridge Page Database

| Property | Notion type | Purpose |
| --- | --- | --- |
| Headline | Title | Page working title |
| Product | Relation | Affiliate Product Database |
| Keyword | Relation | Keyword Database |
| Promise | Rich text | Core user value |
| Sections | Rich text | Structured page outline (JSON or Markdown) |
| Call To Action | Rich text | Primary CTA |
| Disclosure | Rich text | Affiliate disclosure copy |
| Status | Status | Draft → In Review → Approved → Published |
| Created At | Date | Generation timestamp |

## Pinterest Content Database

| Property | Notion type | Purpose |
| --- | --- | --- |
| Title | Title | Pin title |
| Product | Relation | Affiliate Product Database |
| Keyword | Relation | Keyword Database |
| Description | Rich text | Pin description |
| Image Prompt | Rich text | Creative direction for image production |
| Hashtags | Multi-select | Approved tags |
| Destination | URL | Bridge page URL |
| Status | Status | Draft → In Review → Approved → Published |
| Created At | Date | Generation timestamp |

The API client intentionally accepts Notion-native property maps. Mapping these tables to domain objects belongs in repository adapters added with each business use case, keeping the domain module free of storage concerns.

