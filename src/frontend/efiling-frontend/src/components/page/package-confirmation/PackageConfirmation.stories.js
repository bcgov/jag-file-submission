import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import PackageConfirmation from "./PackageConfirmation";

export default {
  title: "PackageConfirmation",
  component: PackageConfirmation
};

const submissionId = "abc123";
const mock = new MockAdapter(axios);
const apiRequest = `/submission/${submissionId}/filing-package`;
const confirmationPopup = getTestData();
const packageConfirmation = { confirmationPopup, submissionId };
const csoAccountStatus = { isNew: false };
const documents = [
  {
    name: "file name 1",
    description: "file description 1",
    type: "file type",
    statutoryFeeAmount: 40
  },
  {
    name: "file name 2",
    description: "file description 2",
    type: "file type",
    statutoryFeeAmount: 0
  }
];

const LoadData = props => {
  mock.onGet(apiRequest).reply(200, { documents: documents });
  return props.children({ packageConfirmation, csoAccountStatus });
};

export const ExistingAccount = () => (
  <LoadData>
    {data => (
      <PackageConfirmation
        packageConfirmation={data.packageConfirmation}
        csoAccountStatus={data.csoAccountStatus}
      />
    )}
  </LoadData>
);

export const NewAccount = () => (
  <LoadData>
    {data => (
      <PackageConfirmation
        packageConfirmation={data.packageConfirmation}
        csoAccountStatus={{ ...data.csoAccountStatus, isNew: true }}
      />
    )}
  </LoadData>
);

export const Mobile = () => (
  <LoadData>
    {data => (
      <PackageConfirmation
        packageConfirmation={data.packageConfirmation}
        csoAccountStatus={data.csoAccountStatus}
      />
    )}
  </LoadData>
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
