import React from "react";
import { createMemoryHistory } from "history";
import { MemoryRouter, Route, Routes } from "react-router";

import UpdateCreditCard from "./UpdateCreditCard";

export default {
  title: "UpdateCreditCard",
  component: UpdateCreditCard,
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const page = { header };

export const Default = () => (
  <MemoryRouter
    initialEntries={[
      "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
    ]}
  >
    <Routes>
      <Route
        element={(routerProps) => (
          <UpdateCreditCard page={page} routerProps={routerProps} />
        )}
        path="/updateCreditCard"
      />
    </Routes>
  </MemoryRouter>
);

export const Mobile = () => (
  <MemoryRouter
    initialEntries={[
      "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
    ]}
  >
    <Routes>
      <Route
        element={(routerProps) => (
          <UpdateCreditCard page={page} routerProps={routerProps} />
        )}
        path="/updateCreditCard"
      />
    </Routes>
  </MemoryRouter>
);

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
