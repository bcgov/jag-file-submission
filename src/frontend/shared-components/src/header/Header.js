/* eslint-disable no-alert */
import React from "react";
import PropTypes from "prop-types";
import "./Header.css";
import bcidSymbol from "../../public/images/bcid-symbol-rev.svg";
import bcidLogoRev from "../../public/images/bcid-logo-rev-en.svg";

export const HeadingTitle = classNames => (
  <div className={classNames} aria-labelledby="title" />
);

export const HeaderImage = (history, classNames, width, src) => (
  <img
    className={classNames}
    src={src}
    width={width}
    height="44"
    alt="B.C. Government Logo"
    onClick={() => history.push("/")}
    role="button"
    onKeyDown={() => history.push("/")}
    tabIndex={0}
  />
);

export default function Header({ header: { name, history } }) {
  return (
    <header>
      <nav className="container-fluid navbar navbar-expand-lg navbar-dark">
        {HeadingTitle("navbar-brand pointer")}
        {HeaderImage(
          history,
          "img-fluid d-none d-md-block pointer",
          181,
          bcidLogoRev
        )}
        {HeaderImage(history, "img-fluid d-md-none pointer", 64, bcidSymbol)}
        {HeadingTitle("pointer navbar-brand nav-item nav-link")}
        <div id="title" className="navbar-brand">
          {name}
        </div>
      </nav>
    </header>
  );
}

Header.propTypes = {
  header: PropTypes.shape({
    name: PropTypes.string.isRequired,
    history: PropTypes.any.isRequired
  }).isRequired
};
