import React from "react";
import PropTypes from "prop-types";

import "./Textarea.css";

export const Textarea = ({ label, onChange }) => {
  return (
    <>
      {label && (
        <div className="text-label">
          <label>{label}</label>
        </div>
      )}
      <textarea
        className="text-input"
        rows="8"
        cols="60"
        onChange={event => onChange(event.target.value)}
      ></textarea>
    </>
  );
};

Textarea.propTypes = {
  label: PropTypes.string,
  onChange: PropTypes.func.isRequired
};

Textarea.defaultProps = {
  label: ""
};
