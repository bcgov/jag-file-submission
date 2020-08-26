import React from "react";
import { MdCreditCard } from "react-icons/md";
import { Alert } from "shared-components";

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
          <a
            href="https://justice.gov.bc.ca/cso/about/index.do"
            target="_blank"
            rel="noopener noreferrer"
          >
            Register a new Credit Card.
          </a>
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
          <a
            href="https://justice.gov.bc.ca/cso/about/index.do"
            target="_blank"
            rel="noopener noreferrer"
          >
            Register a Credit Card now
          </a>
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
