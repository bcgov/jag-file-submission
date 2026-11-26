import { useRouter } from "next/router";
import Cancel from "../src/components/page/cancel/Cancel";

export default function CancelPage() {
  const router = useRouter();

  const header = {
    name: "eFiling Demo Client",
    navigate: router.push.bind(router),
    // shared-components' Header reads header.history.push for the logo click
    history: { push: router.push.bind(router) },
  };

  return <Cancel page={{ header }} />;
}
