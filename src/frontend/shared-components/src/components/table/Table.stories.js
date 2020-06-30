import React from "react";

import Table from "./Table";

export default {
  title: "Table",
  component: Table
};

const tableElements = [
  { name: "BCeID", value: "bobross42" },
  {
    name: "Last Name",
    value: "Ross"
  },
  { name: "First Name", value: "Bob" },
  { name: "Email Address", value: "bob.ross@pbs.com" }
];

const table = {
  header: "BCeID Info",
  tableElements
};

export const WithHeader = () => <Table table={table} />;

export const WithoutHeader = () => <Table table={{ ...table, header: "" }} />;

export const WithBlueStripeStyle = () => (
  <Table table={{ ...table, tableStyle: "blueStripe" }} />
);

export const WithHeaderMobile = () => <Table table={table} />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

WithHeaderMobile.story = mobileViewport;
