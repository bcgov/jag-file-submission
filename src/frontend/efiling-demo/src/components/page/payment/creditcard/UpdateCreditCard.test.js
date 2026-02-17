import React from "react";
import { createMemoryHistory } from "history";
import { render, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router";
import UpdateCreditCard from "./UpdateCreditCard";

describe("Success", () => {
  const header = {
    name: "eFiling Demo Client",
    history: createMemoryHistory(),
  };

  const page = { header };

  window.open = jest.fn();

  test("Component matches the snapshot", () => {
    const { asFragment } = render(
      <MemoryRouter
        initialEntries={[
          "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
        ]}
      >
        <Routes>
          <Route
            path="/updateCreditCard"
            element={<UpdateCreditCard page={page} />}
          />
        </Routes>
      </MemoryRouter>
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("Click on success should redirect to efilinghub with error code 1", () => {
    global.open = jest.fn();

    const { getByText } = render(
      <MemoryRouter
        initialEntries={[
          "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
        ]}
      >
        <Routes>
          <Route
            path="/updateCreditCard"
            element={<UpdateCreditCard page={page} />}
          />
        </Routes>
      </MemoryRouter>
    );

    waitFor(() => getByText("Simulate Card Update Success"));
    fireEvent.click(getByText("Simulate Card Update Success"));

    expect(global.open).toBeCalledWith(
      "http://localhost:3000/efilinghub?responseCode=1",
      "_self"
    );
  });

  test("Click on failure should redirect to efilinghub with error code 19", () => {
    global.open = jest.fn();

    const { getByText } = render(
      <MemoryRouter
        initialEntries={[
          "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
        ]}
      >
        <Routes>
          <Route
            path="/updateCreditCard"
            element={<UpdateCreditCard page={page} />}
          />
        </Routes>
      </MemoryRouter>
    );
    waitFor(() => getByText("Simulate Card Update Failure"));
    fireEvent.click(getByText("Simulate Card Update Failure"));

    expect(global.open).toBeCalledWith("null?responseCode=19", "_self");
  });
});
