import React from "react";
import PropTypes from "prop-types";
import "./Input.scss";

export const Input = function ({
  input: {
    ref,
    label,
    id,
    value,
    placeholder,
    isRequired,
    errorMsg,
    styling,
    note,
    isReadOnly,
    isControlled,
  },
  onChange,
}) {
  let asterisk = "";

  if (isRequired) {
    asterisk = (
      <span id="asterisk" className="bcgov-mandatory">
        *
      </span>
    );
  }

  let labelPart = null;
  const labelPartExists = label !== false;

  if (label) {
    labelPart = (
      <label htmlFor={id} className="bcgov-label">
        <div className="bcgov-input-label">{label}</div>
        {asterisk}&nbsp;
        <span className="bcgov-note">{note}</span>
      </label>
    );
  }

  return (
    <div ref={ref}>
      {labelPartExists && labelPart}
      {isControlled && (
        <input
          className={styling}
          type="text"
          id={id}
          value={value}
          data-testid="input-test"
          placeholder={placeholder}
          readOnly={isReadOnly}
          onChange={(event) => onChange(event.target.value)}
        />
      )}
      {!isControlled && (
        <input
          className={styling}
          type="text"
          id={id}
          defaultValue={value}
          data-testid="input-test"
          placeholder={placeholder}
          readOnly={isReadOnly}
          onChange={(event) => onChange(event.target.value)}
        />
      )}
      <br />
      <span className="bcgov-error" data-testid="email-error">
        {errorMsg}
      </span>
    </div>
  );
};

Input.propTypes = {
  input: PropTypes.shape({
    ref: PropTypes.func,
    label: PropTypes.string,
    id: PropTypes.string.isRequired,
    defaultValue: PropTypes.string,
    value: PropTypes.string,
    placeholder: PropTypes.string,
    isRequired: PropTypes.bool.isRequired,
    errorMsg: PropTypes.string,
    styling: PropTypes.string.isRequired,
    note: PropTypes.string,
    isReadOnly: PropTypes.bool,
    isControlled: PropTypes.bool,
  }).isRequired,
  onChange: PropTypes.func,
};

Input.defaultProps = {
  onChange: () => {},
};
