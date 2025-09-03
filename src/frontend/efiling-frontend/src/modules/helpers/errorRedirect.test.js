import { errorRedirect } from "./errorRedirect";

const errorUrl = "/error.js";

beforeEach(() => {
  window.open = jest.fn().mockImplementation();
});

describe("errorRedirect Test Suite", () => {
  test("errorRedirect null", () => {
    let r = errorRedirect(null, null);
    expect(r).toBeNull();

    r = errorRedirect(errorUrl, null);
    expect(r).toBeNull();

    r = errorRedirect(null, {});
    expect(r).toBeNull();
  });

  test("errorRedirect empty", () => {
    const error = {};
    errorRedirect(errorUrl, error);
    expect(window.open).toHaveBeenCalledWith(
      `${errorUrl}?status=unknown&message=There was an error with your submission.`,
      "_self"
    );
  });

  test("errorRedirect error.status", () => {
    const error = {
      status: 404,
    };
    errorRedirect(errorUrl, error);
    expect(window.open).toHaveBeenCalledWith(
      `${errorUrl}?status=404&message=There was an error with your submission.`,
      "_self"
    );
  });

  test("errorRedirect error.response", () => {
    const error = {
      response: {
        status: 500,
        data: {
          message: "Doh!",
        },
      },
    };
    errorRedirect(errorUrl, error);
    expect(window.open).toHaveBeenCalledWith(
      `${errorUrl}?status=500&message=Doh!`,
      "_self"
    );
  });
});
