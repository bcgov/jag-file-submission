import React from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

export default function PaymentDemo({ page: { header } }) {
  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <p>
            This is the payment demo screen with a single button emulating
            Bambora
          </p>
          <Button
            onClick={() =>
              window.open(
                "http://localhost:3000/efiling?isBambora=true",
                "_self"
              )
            }
            label="Return to efiling hub"
            styling="normal-white btn"
          />
        </div>
      </div>
      <Footer />
    </main>
  );
}

PaymentDemo.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
  }).isRequired,
};
