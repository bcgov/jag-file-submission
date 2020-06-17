import React from "react";
import PropTypes from "prop-types";
import "./Button.css";

export const Button = ({ onClick, label }) => (
  <div className="outline">
    <div className="center-align">
      <button className="efile-btn btn" onClick={onClick} type="button">
        {label}
      </button>
    </div>
  </div>
);

Button.propTypes = {
  onClick: PropTypes.func.isRequired,
  label: PropTypes.string.isRequired
};
