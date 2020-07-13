import React from "react";
import mdx from "./Table.mdx";

import { Table } from "./Table";
import { getTableElementsTestData } from "../../modules/tableTestData";

export default {
  title: "Table",
  component: Table,
  parameters: {
    docs: {
      page: mdx
    }
  }
};

const tableData = getTableElementsTestData();
const header = "BCeID Info";
const basicTableElement = { name: "Key", value: "Value" };
const tableDataWithBoldValues = [{ ...basicTableElement, isValueBold: true }];
const tableDataWithBoldNames = [{ ...basicTableElement, isNameBold: true }];

export const WithHeader = () => <Table heading={header} elements={tableData} />;

export const WithoutHeader = () => <Table elements={tableData} />;

export const WithBlueStripe = () => (
  <Table elements={tableData} styling="blue-stripe" />
);

export const WithBoldValues = () => (
  <Table elements={tableDataWithBoldValues} />
);

export const WithBoldNames = () => <Table elements={tableDataWithBoldNames} />;

export const Mobile = () => <Table heading={header} elements={tableData} />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
