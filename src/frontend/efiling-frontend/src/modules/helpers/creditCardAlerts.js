import React from "react";
import axios from "axios";
import { MdCreditCard } from "react-icons/md";
import { Alert } from "shared-components";
import { errorRedirect } from "./errorRedirect";
import { isEnter } from "./eventUtil";

const registerCard = () => {
  const data = {
    clientId:
      sessionStorage.getItem("internalClientNumber") === "null"
        ? null
        : sessionStorage.getItem("internalClientNumber"),
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

const existingCreditCard = () => (
  <Alert
    icon={<MdCreditCard size={32} />}
    type="success"
    styling="bcgov-success-background bcgov-no-padding-bottom"
    element={
      <p>
        <span>
          You have a valid Credit Card registered with your CSO account.
        </span>
        <br />
        <span
          onClick={() => registerCard()}
          onKeyDown={(e) => {
            if (isEnter(e)) {
              registerCard();
            }
          }}
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

const noCreditCard = () => (
  <Alert
    icon={<MdCreditCard size={32} />}
    type="error"
    styling="bcgov-error-background bcgov-no-padding-bottom"
    element={
      <p>
        <span>
          {sessionStorage.getItem("bamboraErrorExists") === "true" && (
            <strong>Credit Card validation failed.&nbsp;</strong>
          )}
          You do not have a valid Credit Card registered with your CSO account.
        </span>
        <br />
        <span
          onClick={() => registerCard()}
          onKeyDown={(e) => {
            if (isEnter(e)) {
              registerCard();
            }
          }}
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

const failedUpdateCreditCard = () => (
  <Alert
    icon={<MdCreditCard size={32} />}
    type="warning"
    styling="bcgov-warning-background bcgov-no-padding-bottom"
    element={
      <p>
        <span>
          <strong>Credit Card registration failed.&nbsp;</strong>
          You have an existing Credit Card registered with your CSO account and
          can proceed.
        </span>
        <br />
        <span
          onClick={() => registerCard()}
          onKeyDown={(e) => {
            if (isEnter(e)) {
              registerCard();
            }
          }}
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

export function getCreditCardAlerts() {
  const existingCreditCardData = existingCreditCard();
  const noCreditCardData = noCreditCard();
  const failedUpdateCreditCardData = failedUpdateCreditCard();

  return {
    existingCreditCard: existingCreditCardData,
    noCreditCard: noCreditCardData,
    failedUpdateCreditCard: failedUpdateCreditCardData,
  };
}
