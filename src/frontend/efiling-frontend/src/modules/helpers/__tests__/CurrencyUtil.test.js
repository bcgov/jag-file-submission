import { formatCurrency } from "../CurrencyUtil";

describe("CurrencyUtil Testsuite", () => {
  beforeEach(() => {});

  test("expect formatCurrency", () => {
    expect(formatCurrency(1)).toEqual("$1.00");
    expect(formatCurrency(1.15)).toEqual("$1.15");
    expect(formatCurrency(1.234)).toEqual("$1.23");
    expect(formatCurrency(1.236)).toEqual("$1.24");
    expect(formatCurrency(123)).toEqual("$123.00");
    expect(formatCurrency(123.15)).toEqual("$123.15");
    expect(formatCurrency(8123.15)).toEqual("$8,123.15");
  });
});
