import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import Payment from "./Payment";

export default {
  title: "Payment",
  component: Payment
};

sessionStorage.setItem("csoAccountId", "123");

const confirmationPopup = getTestData();
const submissionId = "abc123";

const payment = { confirmationPopup, submissionId };

export const Default = () => <Payment payment={payment} />;

export const Mobile = () => <Payment payment={payment} />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
