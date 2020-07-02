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
  icon: PropTypes.object,
  element: PropTypes.object.isRequired
};

DisplayBox.defaultProps = {
  style: ""
};
