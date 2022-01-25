import React from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

export default function Cancel({ page: { header } }) {
  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <p>
            You have cancelled your submission. Please click the button below to
            go back home.
          </p>
          <Button
            onClick={() => header.navigate("/")}
            label="Return home"
            styling="bcgov-normal-white btn"
            testId="return-home-btn"
          />
        </div>
      </div>
      <Footer />
    </main>
  );
}

Cancel.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
  }).isRequired,
};
