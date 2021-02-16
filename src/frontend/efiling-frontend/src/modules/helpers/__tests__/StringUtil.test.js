import { formatFullName } from "../StringUtil";

describe("StringUtil Testsuite", () => {
  beforeEach(() => {});

  test("formatFullName", () => {
    // full name
    expect(
      formatFullName({
        firstName: "Arthur",
        middleName: "C",
        lastName: "Clark",
      })
    ).toEqual("Clark, Arthur C");

    // missing first
    expect(
      formatFullName({
        firstName: "",
        middleName: "C",
        lastName: "Clark",
      })
    ).toEqual("Clark, C");

    // missing middle
    expect(
      formatFullName({
        firstName: "Arthur",
        middleName: "",
        lastName: "Clark",
      })
    ).toEqual("Clark, Arthur");

    // missing last
    expect(
      formatFullName({
        firstName: "Arthur",
        middleName: "C",
        lastName: "",
      })
    ).toEqual("Arthur C");

    // only first
    expect(
      formatFullName({
        firstName: "Arthur",
        middleName: "",
        lastName: "",
      })
    ).toEqual("Arthur");

    // only middle
    expect(
      formatFullName({
        firstName: "",
        middleName: "C",
        lastName: "",
      })
    ).toEqual("C");

    // only last
    expect(
      formatFullName({
        firstName: "",
        middleName: "",
        lastName: "Clark",
      })
    ).toEqual("Clark");

    expect(formatFullName({})).toEqual("");
  });
});
