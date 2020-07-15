import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import Payment from "./Payment";

export default {
  title: "Payment",
  component: Payment
};

const confirmationPopup = getTestData();

const payment = { confirmationPopup };

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
