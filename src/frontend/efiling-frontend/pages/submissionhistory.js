import dynamic from "next/dynamic";

const AppShell = dynamic(() => import("../src/AppShell"), { ssr: false });
const SubmissionHistory = dynamic(
  () =>
    import("../src/domain/submission/submission-history/SubmissionHistory"),
  { ssr: false }
);

export default function SubmissionHistoryPage() {
  return (
    <AppShell>
      <SubmissionHistory />
    </AppShell>
  );
}
