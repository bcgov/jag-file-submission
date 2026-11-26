import dynamic from "next/dynamic";

// Keycloak, sessionStorage and window.env are browser-only: no SSR/prerender.
const AppShell = dynamic(() => import("../src/AppShell"), { ssr: false });
const Home = dynamic(() => import("../src/components/page/home/Home"), {
  ssr: false,
});

export default function IndexPage() {
  return (
    <AppShell>
      <Home />
    </AppShell>
  );
}
