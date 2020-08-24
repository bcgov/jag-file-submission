import React from "react";
import axios from "axios";
import { MdCreditCard } from "react-icons/md";
import { Alert } from "shared-components";

const registerCard = () => {
  axios
    .post("https://run.mocky.io/v3/fe75e1f6-5129-446e-a02f-267e4c1fb519")
    .then((res) => {
      console.log(res);
      sessionStorage.setItem("validExit", true);
      sessionStorage.setItem("isBamboraRedirect", true);
      window.open(res.data.bamboraUrl, "_self");
    })
    .catch((err) => {
      console.log(err);
    });
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
            className="file-href"
            target="_blank"
            rel="noopener noreferrer"
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
            className="file-href"
            target="_blank"
            rel="noopener noreferrer"
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
