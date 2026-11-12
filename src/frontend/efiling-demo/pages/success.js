import { useRouter } from "next/router";
import Success from "../src/components/page/success/Success";

export default function SuccessPage() {
  const router = useRouter();
  const { packageRef } = router.query;

  const header = {
    name: "eFiling Demo Client",
    navigate: router.push.bind(router),
  };

  return <Success page={{ header, packageRef: packageRef || "" }} />;
}
