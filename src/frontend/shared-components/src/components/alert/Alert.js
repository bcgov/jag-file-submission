import React from "react";
import { MdError, MdCancel, MdCheckBox } from "react-icons/md";
import PropTypes from "prop-types";

import { DisplayBox } from "../display-box/DisplayBox";

const success = (
  <div style={{ color: "#2E8540" }}>
    <MdCheckBox size={32} />
  </div>
);
const warning = (
  <div style={{ color: "rgb(252, 186, 25)" }}>
    <MdError size={32} />
  </div>
);
const danger = (
  <div style={{ color: "#D8292F" }}>
    <MdCancel size={32} />
  </div>
);

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
      {type === "danger" && (
        <DisplayBox
          styling={"danger-background"}
          icon={danger}
          element={element}
        />
      )}
    </>
  );
};
