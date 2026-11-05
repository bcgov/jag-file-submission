import React from "react";
import { render, fireEvent, screen, act, waitFor } from "@testing-library/react";

import RegisterPaymentCard from "../RegisterPaymentCard";

describe("RegisterPaymentCard", () => {
  const originalCustomCheckout = window.customcheckout;
  const originalConsoleError = console.error;
  const originalConsoleDebug = console.debug;

  const setupCustomCheckout = () => {
    const handlers = {};
    const cc = {
      create: jest.fn(() => ({ mount: jest.fn() })),
      on: jest.fn((eventName, cb) => {
        handlers[eventName] = cb;
      }),
      createToken: jest.fn(),
    };

    window.customcheckout = jest.fn(() => cc);
    return { cc, handlers };
  };

  const renderModal = ({ showModal = true, onCreateProfile, onHide } = {}) => {
    const props = {
      showModal,
      onHide: onHide || jest.fn(),
      onCreateProfile,
    };

    return render(<RegisterPaymentCard {...props} />);
  };

  beforeEach(() => {
    console.error = jest.fn();
    console.debug = jest.fn();
  });

  afterEach(() => {
    window.customcheckout = originalCustomCheckout;
    console.error = originalConsoleError;
    console.debug = originalConsoleDebug;
    jest.clearAllMocks();
  });

  test("does not initialize custom checkout when hidden", () => {
    const onCreateProfile = jest.fn();

    window.customcheckout = undefined;

    renderModal({ showModal: false, onCreateProfile });

    expect(window.customcheckout).toBeUndefined();
  });

  test("initializes fields and handles Worldline events", async () => {
    const { handlers } = setupCustomCheckout();
    const onCreateProfile = jest.fn();

    renderModal({ onCreateProfile });

    expect(window.customcheckout).toHaveBeenCalledTimes(1);

    act(() => {
      handlers.brand({ brand: "visa" });
    });

    const cardNumber = document.getElementById("rpb-card-number");
    expect(cardNumber.style.backgroundImage).toContain("visa.svg");

    act(() => {
      handlers.brand({ brand: "unknown" });
    });
    expect(cardNumber.style.backgroundImage).toBe("none");

    act(() => {
      handlers.complete({ field: "card-number" });
      handlers.complete({ field: "cvv" });
      handlers.complete({ field: "expiry" });
    });

    fireEvent.change(screen.getByPlaceholderText("Name on Card"), {
      target: { value: "Jane Doe" },
    });

    expect(screen.getByRole("button", { name: "Register Card" })).toBeEnabled();

    act(() => {
      handlers.error({ field: "cvv", message: "Invalid CVV" });
    });

    expect(document.getElementById("rpb-card-cvv-error").innerText).toBe(
      "Invalid CVV"
    );

    act(() => {
      handlers.empty({ field: "cvv", empty: true });
    });

    expect(screen.getByRole("button", { name: "Register Card" })).toBeDisabled();
  });

  test("shows validation error when name is missing", () => {
    const { handlers, cc } = setupCustomCheckout();
    cc.createToken.mockImplementation(() => {});

    renderModal({ onCreateProfile: jest.fn() });

    act(() => {
      handlers.complete({ field: "card-number" });
      handlers.complete({ field: "cvv" });
      handlers.complete({ field: "expiry" });
    });

    fireEvent.blur(screen.getByPlaceholderText("Name on Card"));
    fireEvent.click(screen.getByRole("button", { name: "Register Card" }));

    expect(screen.getByText("Name on Card is required.")).toBeInTheDocument();
    expect(cc.createToken).not.toHaveBeenCalled();
  });

  test("submit sets required name error", () => {
    setupCustomCheckout();

    renderModal({ onCreateProfile: jest.fn() });

    const form = document.getElementById("checkout-form");
    fireEvent.submit(form);

    expect(screen.getByText("Name on Card is required.")).toBeInTheDocument();
  });

  test("handles tokenization error", () => {
    const { handlers, cc } = setupCustomCheckout();
    cc.createToken.mockImplementation((cb) => cb({ error: "nope" }));

    renderModal({ onCreateProfile: jest.fn() });

    act(() => {
      handlers.complete({ field: "card-number" });
      handlers.complete({ field: "cvv" });
      handlers.complete({ field: "expiry" });
    });

    fireEvent.change(screen.getByPlaceholderText("Name on Card"), {
      target: { value: "Jane Doe" },
    });

    fireEvent.click(screen.getByRole("button", { name: "Register Card" }));

    expect(
      screen.getByText("Error connecting to payment gateway")
    ).toBeInTheDocument();
  });

  test("logs when Worldline init fails", () => {
    const cc = {
      create: jest.fn(() => {
        throw new Error("boom");
      }),
      on: jest.fn(),
      createToken: jest.fn(),
    };

    window.customcheckout = jest.fn(() => cc);

    renderModal({ onCreateProfile: jest.fn() });

    expect(console.error).toHaveBeenCalledWith(
      "Failed to initialize Worldline inputs",
      expect.any(Error)
    );
  });

  test("clears feedback state on resubmit", async () => {
    const { handlers, cc } = setupCustomCheckout();

    cc.createToken.mockImplementation((cb) => cb({ error: "nope" }));

    renderModal({ onCreateProfile: jest.fn() });

    act(() => {
      handlers.complete({ field: "card-number" });
      handlers.complete({ field: "cvv" });
      handlers.complete({ field: "expiry" });
    });

    fireEvent.change(screen.getByPlaceholderText("Name on Card"), {
      target: { value: "Jane Doe" },
    });

    fireEvent.click(screen.getByRole("button", { name: "Register Card" }));

    expect(
      screen.getByText("Error connecting to payment gateway")
    ).toBeInTheDocument();

    cc.createToken.mockImplementation(() => {});

    fireEvent.click(screen.getByRole("button", { name: "Register Card" }));

    await waitFor(() =>
      expect(
        screen.queryByText("Error connecting to payment gateway")
      ).not.toBeInTheDocument()
    );
  });

  test("sets errors for card-number and expiry fields", () => {
    const { handlers } = setupCustomCheckout();

    renderModal({ onCreateProfile: jest.fn() });

    act(() => {
      handlers.error({ field: "card-number", message: "Invalid card" });
      handlers.error({ field: "expiry", message: "Invalid expiry" });
    });

    expect(document.getElementById("rpb-card-number-error").innerText).toBe(
      "Invalid card"
    );
    expect(document.getElementById("rpb-card-expiry-error").innerText).toBe(
      "Invalid expiry"
    );
  });

  test("submit returns early when custom checkout not initialized", () => {
    window.customcheckout = undefined;
    const onCreateProfile = jest.fn();

    renderModal({ onCreateProfile });

    const form = document.getElementById("checkout-form");
    fireEvent.submit(form);

    expect(onCreateProfile).not.toHaveBeenCalled();
  });

  test("handles missing onCreateProfile and API errors, then succeeds", async () => {
    const { handlers, cc } = setupCustomCheckout();
    const onHide = jest.fn();

    cc.createToken.mockImplementation((cb) => cb({ token: "tok-123" }));

    const firstRender = renderModal({ onCreateProfile: null, onHide });

    act(() => {
      handlers.complete({ field: "card-number" });
      handlers.complete({ field: "cvv" });
      handlers.complete({ field: "expiry" });
    });

    fireEvent.change(screen.getByPlaceholderText("Name on Card"), {
      target: { value: "Jane Doe" },
    });

    fireEvent.click(screen.getByRole("button", { name: "Register Card" }));
    expect(
      screen.getByText("Payment profile service is not available.")
    ).toBeInTheDocument();

    firstRender.unmount();

    const onCreateProfile = jest.fn().mockRejectedValue(new Error("fail"));
    const secondRender = renderModal({ onCreateProfile, onHide });

    act(() => {
      handlers.complete({ field: "card-number" });
      handlers.complete({ field: "cvv" });
      handlers.complete({ field: "expiry" });
    });

    fireEvent.change(screen.getByPlaceholderText("Name on Card"), {
      target: { value: "Jane Doe" },
    });

    fireEvent.click(screen.getByRole("button", { name: "Register Card" }));
    expect(
      await screen.findByText("Error creating payment profile.")
    ).toBeInTheDocument();

    secondRender.unmount();

    const onCreateProfileSuccess = jest.fn().mockResolvedValue({});
    renderModal({ onCreateProfile: onCreateProfileSuccess, onHide });

    act(() => {
      handlers.complete({ field: "card-number" });
      handlers.complete({ field: "cvv" });
      handlers.complete({ field: "expiry" });
    });

    fireEvent.change(screen.getByPlaceholderText("Name on Card"), {
      target: { value: "Jane Doe" },
    });

    fireEvent.click(screen.getByRole("button", { name: "Register Card" }));
    expect(onCreateProfileSuccess).toHaveBeenCalledWith({
      tokenCode: "tok-123",
      name: "Jane Doe",
    });
    await waitFor(() => expect(onHide).toHaveBeenCalled());
  });
});