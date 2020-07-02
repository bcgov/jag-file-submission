import React from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

export default function Error({ page: { header, error } }) {
  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-10">
          <p>
            The following error occurred with your eFiling application. Please
            click the button below to go back home.
          </p>
          <p>
            <b>{error}</b>
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
