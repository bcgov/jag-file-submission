import React from "react";
import PropTypes from "prop-types";
import "./Button.css";

export const Button = ({ onClick, label, styling }) => (
  <div>
    <button className={styling} onClick={onClick} type="button">
      {label}
    </button>
  </div>
);

Button.propTypes = {
  onClick: PropTypes.func.isRequired,
  label: PropTypes.string.isRequired,
  styling: PropTypes.string.isRequired
};
