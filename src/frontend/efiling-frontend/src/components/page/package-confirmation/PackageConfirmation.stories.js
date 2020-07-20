import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getDocumentsData } from "../../../modules/documentTestData";

import PackageConfirmation from "./PackageConfirmation";

export default {
  title: "PackageConfirmation",
  component: PackageConfirmation
};

const submissionId = "abc123";
const apiRequest = `/submission/${submissionId}/filing-package`;
const confirmationPopup = getTestData();
const packageConfirmation = { confirmationPopup, submissionId };
const csoAccountStatus = { isNew: false };
const documents = getDocumentsData();

const LoadData = props => {
  const mock = new MockAdapter(axios);
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
