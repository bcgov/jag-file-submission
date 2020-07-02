import React from "react";
import { MdPerson } from "react-icons/md";

import testBasicSnapshot from "../../TestHelper";
import DisplayBox from "./DisplayBox";
import { getTestTable } from "../../modules/displayBoxTestData";

const tableData = getTestTable();
const icon = <MdPerson size={32} />;

describe("DisplayBox Component", () => {
  test("Matches the without icon snapshot", () => {
    testBasicSnapshot(<DisplayBox element={tableData} />);
  });

  test("Matches the with icon snapshot", () => {
    testBasicSnapshot(<DisplayBox icon={icon} element={tableData} />);
  });

  test("Matches the blue-background snapshot", () => {
    testBasicSnapshot(
      <DisplayBox styling={"blue-background"} icon={icon} element={tableData} />
    );
  });
});
