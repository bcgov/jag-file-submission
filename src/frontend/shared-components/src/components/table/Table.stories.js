import React from "react";

import Table from "./Table";
import { getTableTestData } from "../../modules/tableTestData";

export default {
  title: "Table",
  component: Table
};

const tableData = getTableTestData();

export const WithHeader = () => <Table table={tableData} />;

export const WithoutHeader = () => (
  <Table table={{ ...tableData, heading: "" }} />
);

export const WithBlueStripeStyle = () => (
  <Table table={{ ...tableData, style: "blue-stripe" }} />
);

export const WithHeaderMobile = () => <Table table={tableData} />;
export const WithoutHeaderMobile = () => (
  <Table table={{ ...tableData, style: "blue-stripe" }} />
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
