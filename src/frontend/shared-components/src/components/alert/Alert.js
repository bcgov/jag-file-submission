import React from "react";
import PropTypes from "prop-types";

import { DisplayBox } from "../display-box/DisplayBox";
import "./Alert.css";

const generateIcon = (icon, type) => {
  const success = <div style={{ color: "#2E8540" }}>{icon}</div>;
  const warning = <div style={{ color: "rgb(252, 186, 25)" }}>{icon}</div>;
  const error = <div style={{ color: "#D8292F" }}>{icon}</div>;
  const info = <>{icon}</>;

  if (type === "success") return success;
  if (type === "warning") return warning;
  if (type === "error") return error;
  return info;
};

export const Alert = ({ type, styling, element, icon }) => {
  const generatedIcon = generateIcon(icon, type);

  return (
    <DisplayBox styling={styling} icon={generatedIcon} element={element} />
  );
};

Alert.propTypes = {
  type: PropTypes.string.isRequired,
  element: PropTypes.oneOfType([PropTypes.string, PropTypes.object]).isRequired,
  icon: PropTypes.element.isRequired,
  styling: PropTypes.string.isRequired
};
