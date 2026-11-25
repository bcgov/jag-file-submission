import dynamic from "next/dynamic";
import { useRouter } from "next/router";

const AppShell = dynamic(() => import("../../src/AppShell"), { ssr: false });
const PackageReview = dynamic(
  () => import("../../src/domain/package/package-review/PackageReview"),
  { ssr: false }
);

// Exported as a single static shell (packagereview/[packageId].html); nginx
// rewrites /efilinghub/packagereview/<id> to it and the id is read client-side.
export default function PackageReviewPage() {
  const router = useRouter();

  return (
    <AppShell>{router.isReady ? <PackageReview /> : null}</AppShell>
  );
}
