import { useRouter } from "next/router";
import UpdateCreditCard from "../src/components/page/payment/creditcard/UpdateCreditCard";

export default function UpdateCardPage() {
  const router = useRouter();

  const header = {
    name: "eFiling Demo Client",
    navigate: router.push.bind(router),
  };

  return <UpdateCreditCard page={{ header }} />;
}
