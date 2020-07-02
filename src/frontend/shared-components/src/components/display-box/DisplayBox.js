import React from "react";
import PropTypes from "prop-types";

import "./DisplayBox.css";

export default function DisplayBox({ style, icon, element }) {
  return (
    <div className={`display-box ${style}`}>
      <div className={"display-icon"}>{icon}</div>
      <div className={"display-element"}>{element}</div>
    </div>
  );
}

DisplayBox.propTypes = {
  style: PropTypes.string,
  icon: PropTypes.element,
  element: PropTypes.element.isRequired
};

DisplayBox.defaultProps = {
  style: "grey-background",
  icon: ""
};
