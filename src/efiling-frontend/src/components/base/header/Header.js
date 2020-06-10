/* eslint-disable no-alert */
import React from "react";
import PropTypes from "prop-types";
import { useHistory } from "react-router-dom";
import "./Header.css";

export default function Header({ header: { name } }) {
  const history = useHistory();

  return (
    <header>
      <nav className="navbar navbar-expand-lg navbar-dark">
        <div
          className="container-fluid navbar-brand pointer"
          onClick={() => history.push("/")}
          role="button"
          onKeyDown={() => history.push("/")}
          tabIndex={0}
        >
          <img
            className="img-fluid d-none d-md-block"
            src={`${process.env.PUBLIC_URL}/images/bcid-logo-rev-en.svg`}
            width="181"
            height="44"
            alt="B.C. Government Logo"
          />
          <img
            className="img-fluid d-md-none"
            src={`${process.env.PUBLIC_URL}/images/bcid-symbol-rev.svg`}
            width="64"
            height="44"
            alt="B.C. Government Logo"
          />
          <div
            className="pointer navbar-brand nav-item nav-link"
            onClick={() => history.push("/")}
            role="button"
            onKeyDown={() => history.push("/")}
            tabIndex={0}
          >
            {name}
          </div>
        </div>
      </nav>
    </header>
  );
}

Header.propTypes = {
  header: PropTypes.shape({
    name: PropTypes.string.isRequired
  }).isRequired
};
