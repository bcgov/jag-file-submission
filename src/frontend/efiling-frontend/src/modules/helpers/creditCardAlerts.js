import React from "react";
import axios from "axios";
import { MdCreditCard } from "react-icons/md";
import { Alert } from "shared-components";
import { errorRedirect } from "./errorRedirect";

const registerCard = () => {
  const data = {
    clientId:
      sessionStorage.getItem("clientId") === "null"
        ? null
        : Number(sessionStorage.getItem("clientId")),
    redirectUrl: sessionStorage.getItem("bamboraRedirectUrl"),
  };

  axios
    .post("/payment/generate-update-card", data)
    .then(({ data: { bamboraUrl } }) => {
      sessionStorage.setItem("validExit", true);
      sessionStorage.setItem("isBamboraRedirect", true);
      window.open(bamboraUrl, "_self");
    })
    .catch((error) => errorRedirect(sessionStorage.getItem("errorUrl"), error));
};

const existingCreditCard = () => {
  return (
    <Alert
      icon={<MdCreditCard size={32} />}
      type="success"
      styling="success-background no-padding-bottom"
      element={
        <p>
          <span>
            You have a valid Credit Card registered with your CSO account.
          </span>
          <br />
          <span
            onClick={() => registerCard()}
            onKeyDown={() => registerCard()}
            className="file-href"
            role="button"
            tabIndex={0}
          >
            Register a new Credit Card.
          </span>
        </p>
      }
    />
  );
};

const noCreditCard = () => {
  return (
    <Alert
      icon={<MdCreditCard size={32} />}
      type="error"
      styling="error-background no-padding-bottom"
      element={
        <p>
          <span>
            You do not have a valid Credit Card registered with your CSO
            account.
          </span>
          <br />
          <span
            onClick={() => registerCard()}
            onKeyDown={() => registerCard()}
            className="file-href"
            role="button"
            tabIndex={0}
          >
            Register a Credit Card now
          </span>
          &nbsp;to continue.
        </p>
      }
    />
  );
};

export function getCreditCardAlerts() {
  const existingCreditCardData = existingCreditCard();
  const noCreditCardData = noCreditCard();

  return {
    existingCreditCard: existingCreditCardData,
    noCreditCard: noCreditCardData,
  };
}
