import React from "react";
import { createMemoryHistory } from "history";
import PackageReview from "./PackageReview";

export default {
  title: "PackageReview",
  component: PackageReview,
};

const header = {
  name: "E-File Submission",
  history: createMemoryHistory(),
};
const packageId = "1";

const page = {
  header,
  packageId,
};

export const Default = () => <PackageReview page={page} />;

export const Mobile = () => <PackageReview page={page} />;

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
