import React from "react";
import { createMemoryHistory } from "history";
import { render, fireEvent, getByText } from "@testing-library/react";
import { MemoryRouter, Route } from "react-router";
import UpdateCreditCard from "./UpdateCreditCard";

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const page = { header };

window.open = jest.fn();

describe("Success", () => {
  test("Component matches the snapshot", () => {
    const { asFragment } = render(
      <MemoryRouter
        initialEntries={[
          "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
        ]}
      >
        <Route
          component={(routerProps) => (
            <UpdateCreditCard page={page} routerProps={routerProps} />
          )}
          path="/updateCreditCard"
        />
      </MemoryRouter>
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("Click on failure should redirect to efilinghub with error code 19", () => {
    global.open = jest.fn();

    const { container } = render(
      <MemoryRouter
        initialEntries={[
          "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
        ]}
      >
        <Route
          component={(routerProps) => (
            <UpdateCreditCard page={page} routerProps={routerProps} />
          )}
          path="/updateCreditCard"
        />
      </MemoryRouter>
    );

    fireEvent.click(getByText(container, "Simulate Card Update Failure"));

    expect(global.open).toBeCalledWith(
      "http://localhost:3000/efilinghub?responseCode=19",
      "_self"
    );
  });

  test("Click on success should redirect to efilinghub with error code 1", () => {
    global.open = jest.fn();

    const { container } = render(
      <MemoryRouter
        initialEntries={[
          "/updateCreditCard?serviceVersion=1&merchantId=merchantid&trnLanguage=eng&operationType=N&ref1=customerCode&trnReturnURL=http://localhost:3000/efilinghub&trnOrderNumber=C1234&hashValue=97F9D18E87F902807890CBF8B11D3244&hashExpiry=202012302048",
        ]}
      >
        <Route
          component={(routerProps) => (
            <UpdateCreditCard page={page} routerProps={routerProps} />
          )}
          path="/updateCreditCard"
        />
      </MemoryRouter>
    );

    fireEvent.click(getByText(container, "Simulate Card Update Success"));

    expect(global.open).toBeCalledWith(
      "http://localhost:3000/efilinghub?responseCode=1",
      "_self"
    );
  });
});
