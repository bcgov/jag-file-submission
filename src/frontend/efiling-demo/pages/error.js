import { useRouter } from "next/router";
import ErrorPage from "../src/components/page/error/Error";

export default function ErrorRoute() {
  const router = useRouter();
  const { status, message } = router.query;

  const header = {
    name: "eFiling Demo Client",
    navigate: router.push.bind(router),
    // shared-components' Header reads header.history.push for the logo click
    history: { push: router.push.bind(router) },
  };

  return (
    <ErrorPage
      page={{ header, status: status || "", message: message || "" }}
    />
  );
}
