import React from "react";
import { MdError, MdCancel, MdCheckBox, MdInfo } from "react-icons/md";
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
const error = (
  <div style={{ color: "#D8292F" }}>
    <MdCancel size={32} />
  </div>
);
const info = <MdInfo size={32} />;

export const Alert = ({ type, element }) => {
  return (
    <>
      {type === "success" && (
        <DisplayBox
          styling="success-background"
          icon={success}
          element={element}
        />
      )}
      {type === "warning" && (
        <DisplayBox
          styling="warning-background"
          icon={warning}
          element={element}
        />
      )}
      {type === "error" && (
        <DisplayBox styling="error-background" icon={error} element={element} />
      )}
      {type === "info" && (
        <DisplayBox styling="info-background" icon={info} element={element} />
      )}
    </>
  );
};

Alert.propTypes = {
  type: PropTypes.string.isRequired,
  element: PropTypes.string.isRequired
};
