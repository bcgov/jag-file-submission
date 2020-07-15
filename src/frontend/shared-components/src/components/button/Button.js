import React from "react";
import PropTypes from "prop-types";
import "./Button.css";

export const Button = ({ onClick, label, styling, disabled, testId }) => (
  <div>
    <button
      className={styling}
      onClick={onClick}
      type="button"
      disabled={disabled}
      data-test-id={testId}
    >
      {label}
    </button>
  </div>
);

Button.propTypes = {
  onClick: PropTypes.func.isRequired,
  label: PropTypes.string.isRequired,
  styling: PropTypes.string.isRequired,
  disabled: PropTypes.bool,
  testId: PropTypes.string
};

Button.defaultProps = {
  disabled: false,
  testId: ""
};
