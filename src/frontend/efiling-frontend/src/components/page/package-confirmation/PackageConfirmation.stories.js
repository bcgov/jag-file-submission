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
    name: "file name",
    description: "file description",
    type: "file type",
    statutoryFeeAmount: 40
  }
];

const LoadData = props => {
  mock.onGet(apiRequest).reply(200, { documents });
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
