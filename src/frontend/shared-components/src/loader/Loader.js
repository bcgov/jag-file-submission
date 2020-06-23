import React from "react";
import PropTypes from "prop-types";
import "./Loader.css";

export const Loader = ({ page }) => {
  let loaderCss = "btn-loader";
  if (page) loaderCss = "page-loader";

  return (
    <div className="loader-align">
      <div className={loaderCss} />
      <br />
      Loading... Please Wait
    </div>
  );
};

Loader.propTypes = {
  page: PropTypes.bool
};

Loader.defaultProps = {
  page: false
};
