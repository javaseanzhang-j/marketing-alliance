"use client";

import { useMemo, useState } from "react";

type ProductRow = {
  service: string;
  seller: string;
  category: string;
  price: string;
  score: number;
  demand: "High" | "Medium";
  status: "Ready" | "Analyzing" | "Review";
};

const products: ProductRow[] = [
  { service: "WordPress speed optimization", seller: "speedcraft", category: "Programming & Tech", price: "$40", score: 92, demand: "High", status: "Ready" },
  { service: "Minimal brand identity system", seller: "northstudio", category: "Graphics & Design", price: "$65", score: 88, demand: "High", status: "Ready" },
  { service: "SEO blog content package", seller: "wordfoundry", category: "Writing & Translation", price: "$55", score: 84, demand: "High", status: "Review" },
  { service: "Short-form video editing", seller: "cutroom", category: "Video & Animation", price: "$35", score: 79, demand: "Medium", status: "Analyzing" },
  { service: "Shopify conversion audit", seller: "growthbench", category: "Business", price: "$80", score: 76, demand: "Medium", status: "Review" },
];

const navItems = [
  ["Overview", "⌂"],
  ["Products", "◇"],
  ["Keywords", "↗"],
  ["Opportunities", "◎"],
  ["Bridge Pages", "▤"],
  ["Pinterest", "◫"],
];

export default function Home() {
  const [active, setActive] = useState("Overview");
  const [query, setQuery] = useState("");

  const visibleProducts = useMemo(() => {
    const normalized = query.trim().toLowerCase();
    if (!normalized) return products;
    return products.filter((product) =>
      [product.service, product.seller, product.category].some((value) =>
        value.toLowerCase().includes(normalized),
      ),
    );
  }, [query]);

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <span className="brand-mark">AI</span>
          <span>Affiliate<br />Intelligence</span>
        </div>

        <p className="eyebrow sidebar-label">Workspace</p>
        <nav aria-label="Primary navigation">
          {navItems.map(([label, icon]) => (
            <button
              className={`nav-item ${active === label ? "active" : ""}`}
              key={label}
              onClick={() => setActive(label)}
              type="button"
            >
              <span className="nav-icon" aria-hidden="true">{icon}</span>
              {label}
              {label === "Opportunities" && <span className="nav-count">18</span>}
            </button>
          ))}
        </nav>

        <div className="sidebar-spacer" />
        <div className="notion-card">
          <div className="notion-card-head">
            <span className="notion-glyph">N</span>
            <div><strong>Notion sync</strong><small>Business database</small></div>
          </div>
          <div className="sync-row"><span><i /> Connected</span><span>2m ago</span></div>
        </div>
        <button className="nav-item settings" type="button"><span className="nav-icon">⚙</span>Settings</button>
        <div className="profile">
          <span className="avatar">XZ</span>
          <span><strong>Xiaofeng Zhang</strong><small>Growth workspace</small></span>
          <span className="profile-more">•••</span>
        </div>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <div className="mobile-brand"><span className="brand-mark">AI</span><b>Affiliate Intelligence</b></div>
          <label className="search-box">
            <span aria-hidden="true">⌕</span>
            <input
              aria-label="Search products, keywords and pages"
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Search products, keywords, pages..."
              value={query}
            />
            <kbd>⌘ K</kbd>
          </label>
          <button className="icon-button" aria-label="Notifications" type="button">♢<i /></button>
          <button className="primary-button" type="button"><span>＋</span> New analysis</button>
        </header>

        <div className="content">
          <div className="page-heading">
            <div>
              <p className="eyebrow">Tuesday · July 14</p>
              <h1>{active === "Overview" ? "Good afternoon, Xiaofeng." : active}</h1>
              <p>{active === "Overview" ? "Here’s where your best affiliate opportunities are forming." : `Manage your ${active.toLowerCase()} intelligence and workflow.`}</p>
            </div>
            <div className="heading-actions">
              <button className="secondary-button" type="button">Last 30 days <span>⌄</span></button>
              <button className="secondary-button" type="button">↥ Export</button>
            </div>
          </div>

          <section className="metric-grid" aria-label="Key performance metrics">
            <Metric title="Products tracked" value="247" delta="12.4%" detail="28 added this month" tone="teal" spark={[24, 29, 27, 35, 33, 43, 47]} />
            <Metric title="Active keywords" value="84" delta="8.1%" detail="11 awaiting review" tone="clay" spark={[20, 19, 25, 23, 30, 34, 38]} />
            <Metric title="High opportunities" value="18" delta="5 new" detail="Score above 80" tone="amber" spark={[18, 24, 21, 28, 26, 34, 41]} />
            <Metric title="Content drafts" value="42" delta="7 ready" detail="Bridge pages & pins" tone="plum" spark={[17, 22, 20, 29, 27, 31, 39]} />
          </section>

          <section className="insight-card">
            <div className="insight-copy">
              <span className="ai-chip"><b>✦</b> AI opportunity brief</span>
              <h2>Site-speed services are moving into a high-conversion window.</h2>
              <p>Demand is up 23% while competition remains moderate. Three products now match your strongest transactional keywords.</p>
              <button className="text-button" type="button">Review 3 opportunities <span>→</span></button>
            </div>
            <div className="score-orbit" aria-label="Opportunity score 92 out of 100">
              <div className="score-ring"><strong>92</strong><span>Opportunity<br />score</span></div>
              <span className="orbit-note note-one"><i /> High demand</span>
              <span className="orbit-note note-two"><i /> Strong intent</span>
            </div>
          </section>

          <div className="section-row">
            <div><h2>Top opportunities</h2><p>Ranked from current demand, competition, payout and content fit.</p></div>
            <button className="text-button muted" type="button">View all opportunities <span>→</span></button>
          </div>

          <section className="table-card">
            <div className="table-toolbar">
              <div className="filter-pills" role="group" aria-label="Opportunity filters">
                <button className="selected" type="button">All <span>{visibleProducts.length}</span></button>
                <button type="button">High potential <span>3</span></button>
                <button type="button">Needs review <span>2</span></button>
              </div>
              <button className="table-filter" type="button">☷ Filter</button>
            </div>
            <div className="table-scroll">
              <table>
                <thead><tr><th>Service</th><th>Category</th><th>Starting at</th><th>Demand</th><th>AI score</th><th>Status</th><th><span className="sr-only">Actions</span></th></tr></thead>
                <tbody>
                  {visibleProducts.map((product, index) => (
                    <tr key={product.service}>
                      <td><div className={`service-icon icon-${index % 5}`}>{["WP", "BR", "SE", "VD", "SH"][index % 5]}</div><span><strong>{product.service}</strong><small>by {product.seller}</small></span></td>
                      <td>{product.category}</td>
                      <td className="price">{product.price}</td>
                      <td><span className={`demand ${product.demand.toLowerCase()}`}><i />{product.demand}</span></td>
                      <td><div className="score-cell"><b>{product.score}</b><span><i style={{ width: `${product.score}%` }} /></span></div></td>
                      <td><span className={`status ${product.status.toLowerCase()}`}>{product.status}</span></td>
                      <td><button className="row-action" aria-label={`Open ${product.service}`} type="button">•••</button></td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {visibleProducts.length === 0 && <div className="empty-state">No opportunities match “{query}”.</div>}
            </div>
          </section>

          <footer><span><i /> Intelligence refreshed 2 minutes ago</span><span>Data source: Notion · Mock providers</span></footer>
        </div>
      </section>
    </main>
  );
}

function Metric({ title, value, delta, detail, tone, spark }: { title: string; value: string; delta: string; detail: string; tone: string; spark: number[] }) {
  return (
    <article className={`metric-card ${tone}`}>
      <div className="metric-top"><span>{title}</span><button aria-label={`${title} details`} type="button">•••</button></div>
      <div className="metric-main"><strong>{value}</strong><span className="metric-delta">↗ {delta}</span></div>
      <div className="metric-bottom"><small>{detail}</small><span className="spark-bars" role="img" aria-label={`${title} rising trend`}>{spark.map((height, index) => <i key={index} style={{ height: `${height}%` }} />)}</span></div>
    </article>
  );
}
