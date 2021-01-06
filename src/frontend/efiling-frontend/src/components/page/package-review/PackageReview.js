import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";
import axios from "axios";
import { propTypes } from "../../../types/propTypes";

export default function PackageReview({ page: { header, packageId } }) {
  useEffect(() => {
    console.log(packageId);
    axios
      .get(`filingpackage/${packageId}`)
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  });

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-8">
          <h1>Package Review!</h1>
          <br />
          <p>{`We are dealing with package ID: ${packageId}`}</p>
          <br />
          <section className="buttons pt-2">
            <Button
              label="Cancel and Return to Parent App"
              onClick={() => window.open("http://google.com", "_self")}
              styling="normal-white btn"
            />
          </section>
        </div>
      </div>
      <Footer />
    </main>
  );
}

PackageReview.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    packageId: PropTypes.string.isRequired,
  }).isRequired,
};
