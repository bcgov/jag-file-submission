import React from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import { Header, Footer, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

export default function Error({ page: { header, error } }) {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  const status = queryParams.status;
  const message = queryParams.message;

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <p>
            The following error occurred with your submission. Please click the
            button below to go back home.
          </p>
          <p>
            <b>{error}</b>
            <b>{status}</b>
            <b>{message}</b>
          </p>
          <br />
          <Button
            onClick={() => header.history.push("/")}
            label="Return home"
            styling="normal-white btn"
          />
        </div>
      </div>
      <Footer />
    </main>
  );
}

Error.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    error: PropTypes.string.isRequired
  }).isRequired
};
