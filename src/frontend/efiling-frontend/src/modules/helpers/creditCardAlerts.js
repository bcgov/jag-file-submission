import React from "react";
import { MdCreditCard } from "react-icons/md";
import { Alert } from "shared-components";
import { isEnter } from "./eventUtil";

const existingCreditCard = (onRegister) => (
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
          onClick={() => (onRegister())}
          onKeyDown={(e) => {
            if (isEnter(e)) {
              onRegister();
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

const noCreditCard = (onRegister) => (
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
          onClick={() => (onRegister())}
          onKeyDown={(e) => {
            if (isEnter(e)) {
              onRegister();
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

const failedUpdateCreditCard = (onRegister) => (
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
          onClick={() => (onRegister())}
          onKeyDown={(e) => {
            if (isEnter(e)) {
              onRegister();
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

export function getCreditCardAlerts(onRegister) {
  const existingCreditCardData = existingCreditCard(onRegister);
  const noCreditCardData = noCreditCard(onRegister);
  const failedUpdateCreditCardData = failedUpdateCreditCard(onRegister);

  return {
    existingCreditCard: existingCreditCardData,
    noCreditCard: noCreditCardData,
    failedUpdateCreditCard: failedUpdateCreditCardData,
  };
}
