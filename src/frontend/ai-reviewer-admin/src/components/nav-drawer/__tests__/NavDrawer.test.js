import React from "react";
import { render, act, fireEvent, waitFor} from "@testing-library/react";
import {BrowserRouter as Router} from "react-router-dom"
import matchMediaPolyfill from 'mq-polyfill'
import NavDrawer from "../NavDrawer";

describe("NavDrawer test suite", () => {

  beforeAll(() => {
    matchMediaPolyfill(window)
    window.resizeTo = function resizeTo(width, height) {
      Object.assign(this, {
        innerWidth: width,
        innerHeight: height,
        outerWidth: width,
        outerHeight: height,
      }).dispatchEvent(new Event('resize'))
  }}
  )

  test("Renders temporary drawer", async () => {
    const {getByTestId, getByText} = render(<Router><NavDrawer variant="temporary" /></Router>);

    act(() => {
      window.resizeTo(400, 1000)
    })
    
    const button = getByTestId("nav-btn")
    expect(button).toBeInTheDocument();

    fireEvent.click(button)
    await waitFor(() => {})

    expect(getByText("Document Type Configuration")).toBeInTheDocument()
    
  });

  test("Renders permanent drawer", () => {
    const { getByText} = render(<Router><NavDrawer variant="permanent" /></Router>);

    act(() => {
      window.resizeTo(1200, 1000)
    })

    expect(getByText("Document Type Configuration")).toBeInTheDocument()
  });

})