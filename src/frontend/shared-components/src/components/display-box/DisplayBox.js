import React from "react";
import PropTypes from "prop-types";

import "./DisplayBox.css";

export const DisplayBox = ({ styling, icon, element }) => {
  return (
    <div className={`display-box ${styling}`}>
      {icon && <div className="display-icon">{icon}</div>}
      <div className="display-right-element">{element}</div>
    </div>
  );
};

DisplayBox.propTypes = {
  styling: PropTypes.string,
  icon: PropTypes.element,
  element: PropTypes.oneOfType([PropTypes.string, PropTypes.element]).isRequired
};

DisplayBox.defaultProps = {
  styling: "",
  icon: null
};
