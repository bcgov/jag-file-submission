import dynamic from "next/dynamic";
import { useRouter } from "next/router";

// Keycloak touches the DOM and localStorage — must be client-side only
const AuthenticationGuard = dynamic(
  () => import("../src/components/hoc/AuthenticationGuard"),
  { ssr: false }
);

export default function IndexPage() {
  const router = useRouter();

  const header = {
    name: "eFiling Demo Client",
    navigate: router.push.bind(router),
  };

  return <AuthenticationGuard page={{ header }} />;
}
