import React from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

export default function Success({ page: { header, packageRef } }) {
  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <p>
            You have successfully completed your submission. Click the link
            below to reference your submitted package.
          </p>
          <p
            onClick={() => {
              const buff = new Buffer(packageRef, "base64");
              const url = buff.toString("ascii");
              window.open(url);
            }}
            className="href"
          >
            View my submitted package.
          </p>
          <br />
          <p>Please click the button below to go back home.</p>
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

Success.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    packageRef: PropTypes.string.isRequired,
  }).isRequired,
};
