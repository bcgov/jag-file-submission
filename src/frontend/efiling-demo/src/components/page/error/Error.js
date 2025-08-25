import React from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";
import "./Error.css";

export default function Error({ page: { header, status, message } }) {
  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <p>
            The following error occurred with your submission. Please click the
            button below to go back home.
          </p>
          <p className="error-info">
            <b>
              Status:&nbsp;
              {status}
            </b>
            <b>
              Message:&nbsp;
              {message}
            </b>
          </p>
          <br />
          <Button
            onClick={() => header.navigate("/")}
            label="Return home"
            styling="bcgov-normal-white btn"
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
    status: PropTypes.string.isRequired,
    message: PropTypes.string.isRequired,
  }).isRequired,
};
