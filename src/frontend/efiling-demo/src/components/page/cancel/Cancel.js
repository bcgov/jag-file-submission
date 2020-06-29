import React from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";

export default function Cancel({ page: { header } }) {
  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-10">
          <p>
            You have cancelled your eFiling application. Please click the button
            below to go back home.
          </p>
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

Cancel.propTypes = {
  page: PropTypes.shape({
    header: PropTypes.shape({
      name: PropTypes.string.isRequired
    }).isRequired
  }).isRequired
};
