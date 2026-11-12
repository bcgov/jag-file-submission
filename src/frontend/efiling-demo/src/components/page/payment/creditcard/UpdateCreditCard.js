import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { Footer, Header, Button, Table } from "shared-components";
import { useRouter } from "next/router";

import { propTypes } from "../../../../types/propTypes";

export default function UpdateCreditCard({ page: { header } }) {
  const successCode = 1;
  const errorCode = 19;

  const [queryParams, setqueryParams] = useState([]);
  const [redirectUrl, setRedirectUrl] = useState(null);
  const router = useRouter();

  const redirect = (code) => {
    window.open(`${redirectUrl}?responseCode=${code}`, "_self");
  };

  useEffect(() => {
    const paramters = [];

    const queryStrings = router.query;

    Object.keys(queryStrings).forEach((key) => {
      paramters.push({
        name: key,
        value: queryStrings[key],
        isValueBold: true,
      });
    });

    setRedirectUrl(queryStrings.trnReturnURL);
    setqueryParams(paramters);
  }, [router.query]);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content container">
          <div className="row justify-content-md-center">
            <div className="col-md-8 col-sm-12">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Query String values</h5>
                  <Table elements={queryParams} />
                </div>
              </div>
            </div>
          </div>
          <br />
          <div className="row justify-content-md-center">
            <div className="col-md-4 col-sm-12 mt-2">
              <Button
                onClick={() => {
                  redirect(successCode);
                }}
                label="Simulate Card Update Success"
                styling="btn btn-success mx-auto"
              />
            </div>
            <div className="col-md-4 col-sm-12 mt-2">
              <Button
                onClick={() => {
                  redirect(errorCode);
                }}
                label="Simulate Card Update Failure"
                styling="btn btn-danger mx-auto"
              />
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </main>
  );
}

UpdateCreditCard.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
  }).isRequired,
};
