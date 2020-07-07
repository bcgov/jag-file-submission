import React from "react";

import { Table } from "./Table";
import { getTableElementsTestData } from "../../modules/tableTestData";

export default {
  title: "Table",
  component: Table
};

const tableData = getTableElementsTestData();
const header = "BCeID Info";

export const WithHeader = () => <Table heading={header} elements={tableData} />;

export const WithoutHeader = () => <Table elements={tableData} />;

export const WithBlueStripeStyle = () => (
  <Table elements={tableData} styling="blue-stripe" />
);

export const WithHeaderMobile = () => (
  <Table heading={header} elements={tableData} />
);
export const WithoutHeaderMobile = () => (
  <Table elements={tableData} styling="blue-stripe" />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

WithHeaderMobile.story = mobileViewport;
WithoutHeaderMobile.story = mobileViewport;
