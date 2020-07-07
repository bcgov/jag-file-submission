import React from "react";
import { MdError, MdBlock, MdCheckBox } from "react-icons/md";
import PropTypes from "prop-types";

import { DisplayBox } from "../display-box/DisplayBox";

const success = (
  <div style={{ color: "rgb(252,186,25)" }}>
    <MdCheckBox size={32} />
  </div>
);
const warning = <MdError size={32} />;
const error = <MdBlock size={32} />;

export const Alert = ({ type, element }) => {
  return (
    <>
      {type === "success" && (
        <DisplayBox
          styling={"success-background"}
          icon={success}
          element={element}
        />
      )}
      {type === "warning" && (
        <DisplayBox
          styling={"warning-background"}
          icon={warning}
          element={element}
        />
      )}
      {type === "error" && (
        <DisplayBox
          styling={"error-background"}
          icon={error}
          element={element}
        />
      )}
    </>
  );
};
