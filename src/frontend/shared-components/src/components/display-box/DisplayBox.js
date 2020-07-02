import React from "react";
import PropTypes from "prop-types";

import "./DisplayBox.css";

export default function DisplayBox({ styling, icon, element }) {
  return (
    <div className={`display-box ${styling}`}>
      <div className={"display-icon"}>{icon}</div>
      <div className={"display-element"}>{element}</div>
    </div>
  );
}

DisplayBox.propTypes = {
  styling: PropTypes.string,
  icon: PropTypes.element,
  element: PropTypes.element.isRequired
};

DisplayBox.defaultProps = {
  styling: "grey-background",
  icon: ""
};
