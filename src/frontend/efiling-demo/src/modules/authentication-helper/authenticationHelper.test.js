import { getJWTData } from "./authenticationHelper";

describe("Authentication helper", () => {
  test("getJWTData returns null when no decoded object or no decoded payload", () => {
    const result = getJWTData();

    expect(result).toEqual(null);
  });
});
