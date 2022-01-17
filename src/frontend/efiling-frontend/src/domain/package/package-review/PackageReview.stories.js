/* eslint-disable no-var */
/* eslint-disable  */
/* eslint-disable vars-on-top */
import React from "react";
import PackageReview from "./PackageReview";

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useParams: jest.fn().mockReturnValue({ packageId: 1 }),
  useLocation: jest
    .fn()
    .mockReturnValue({ search: "?returnUrl=http://www.google.com" }),
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
