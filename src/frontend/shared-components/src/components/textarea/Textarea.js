import React from "react";
import PropTypes from "prop-types";

import "./Textarea.css";

export const Textarea = ({ id, label, onChange }) => {
  return (
    <>
      {label && (
        <div className="text-label">
          <label htmlFor={id}>{label}</label>
        </div>
      )}
      <textarea
        id={id}
        className="text-input"
        rows="8"
        cols="60"
        onChange={event => onChange(event.target.value)}
      />
    </>
  );
};

Textarea.propTypes = {
  id: PropTypes.string.isRequired,
  label: PropTypes.string,
  onChange: PropTypes.func.isRequired
};

Textarea.defaultProps = {
  label: ""
};
