/* eslint-disable no-var */
/* eslint-disable  */
/* eslint-disable vars-on-top */
import React from "react";
import PackageReview from "./PackageReview";

jest.mock("next/router", () => ({
  useRouter: jest.fn().mockReturnValue({
    query: { packageId: 1, returnUrl: "http://www.google.com" },
  }),
}));

export default {
  title: "PackageReview",
  component: PackageReview,
};

export var Default = function () {
  return <PackageReview />;
};

export var Mobile = function () {
  return <PackageReview />;
};

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
