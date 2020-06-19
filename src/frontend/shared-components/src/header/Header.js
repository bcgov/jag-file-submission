/* eslint-disable no-alert */
import React from "react";
import PropTypes from "prop-types";
import { useHistory } from "react-router-dom";
import "./Header.css";
import bcidSymbol from "../../public/images/bcid-symbol-rev.svg";
import bcidLogoRev from "../../public/images/bcid-logo-rev-en.svg";

export const HeadingTitle = (history, classNames) => (
  <div
    className={classNames}
    onClick={() => history.push("/")}
    role="button"
    onKeyDown={() => history.push("/")}
    tabIndex={0}
    aria-labelledby="title"
  />
);

export const HeaderImage = (classNames, width, src) => (
  <img
    className={classNames}
    src={src}
    width={width}
    height="44"
    alt="B.C. Government Logo"
  />
);

export const Header = ({ header: { name } }) => {
  const history = useHistory();

  return (
    <header>
      <nav className="container-fluid navbar navbar-expand-lg navbar-dark">
        {HeadingTitle(history, "navbar-brand pointer")}
        {HeaderImage("img-fluid d-none d-md-block", 181, bcidLogoRev)}
        {HeaderImage("img-fluid d-md-none", 64, bcidSymbol)}
        {HeadingTitle(history, "pointer navbar-brand nav-item nav-link")}
        <div id="title" className="navbar-brand">
          {name}
        </div>
      </nav>
    </header>
  );
};

Header.propTypes = {
  header: PropTypes.shape({
    name: PropTypes.string.isRequired
  }).isRequired
};
