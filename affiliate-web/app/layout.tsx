import type { Metadata } from "next";
import { headers } from "next/headers";
import "./globals.css";

export async function generateMetadata(): Promise<Metadata> {
  const requestHeaders = await headers();
  const host = requestHeaders.get("x-forwarded-host") ?? requestHeaders.get("host") ?? "localhost:3000";
  const protocol = requestHeaders.get("x-forwarded-proto") ?? (host.startsWith("localhost") ? "http" : "https");
  const metadataBase = new URL(`${protocol}://${host}`);

  return {
    title: "AI Affiliate Intelligence",
    description: "Discover high-value affiliate offers and turn intelligence into content that converts.",
    metadataBase,
    openGraph: {
      title: "AI Affiliate Intelligence",
      description: "Discover better offers. Build content that converts.",
      images: [{ url: "/og.png", width: 1672, height: 941, alt: "AI Affiliate Intelligence opportunity dashboard" }],
    },
    twitter: { card: "summary_large_image", images: ["/og.png"] },
  };
}

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="en"><body>{children}</body></html>;
}
