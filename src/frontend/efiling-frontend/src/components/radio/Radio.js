/* eslint-disable  */
/* eslint-disable no-var */
/* eslint-disable vars-on-top */
import React from "react";
import PropTypes from "prop-types";

import "./Radio.scss";

export var Radio = function ({ id, name, label, onSelect, defaultChecked }) {
  return (
    <label className="bcgov-radio" htmlFor={id}>
      {label}
      <input
        type="radio"
        name={name}
        id={id}
        onChange={(e) => onSelect(e.target.id)}
        defaultChecked={defaultChecked}
      />
      <span className="bcgov-dot" />
    </label>
  );
};

Radio.propTypes = {
  label: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  onSelect: PropTypes.func,
  defaultChecked: PropTypes.bool,
};

Radio.defaultProps = {
  onSelect: () => {},
  defaultChecked: false,
};
