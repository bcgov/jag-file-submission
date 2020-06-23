import React from "react";
import PropTypes from "prop-types";
import "./Input.css";

export const Input = ({
  input: { label, id, value, placeholder, isRequired, errorMsg, styling, note },
  onChange
}) => {
  let asterisk = "";

  if (isRequired) {
    asterisk = (
      <span id="asterisk" className="mandatory">
        *
      </span>
    );
  }

  let labelPart = null;
  const labelPartExists = label !== false;

  if (label) {
    labelPart = (
      <label htmlFor={id} className="label">
        <div className="textinput_label">{label}</div>
        {asterisk}&nbsp;
        <span className="note">{note}</span>
      </label>
    );
  }

  return (
    <div>
      {labelPartExists && labelPart}
      <input
        className={styling}
        type="text"
        id={id}
        defaultValue={value}
        placeholder={placeholder}
        onChange={event => onChange(event.target.value)}
      />
      <br />
      <span className="error">{errorMsg}</span>
    </div>
  );
};
