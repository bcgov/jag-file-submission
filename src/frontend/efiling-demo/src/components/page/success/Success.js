import React from "react";
import PropTypes from "prop-types";
import { Header, Footer, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

export default function Success({ page: { header, packageRef } }) {
  const openPackageRef = () => {
    const buff = Buffer.from(packageRef, "base64");
    const returnUrl =
      "https%3A%2F%2Fwww.google.ca%2Fsearch%3Fq%3Dbob%2Bross%26tbm%3Disch";
    let url = `${buff.toString("ascii")}`;
    url += url.indexOf("?") < 0 ? "?" : "&";
    url += `returnUrl=${returnUrl}`;
    url += `&returnAppName=Parent%20App`;
    window.open(url);
  };

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <p>
            You have successfully completed your submission. Click the link
            below to reference your submitted package.
          </p>
          <span
            onClick={() => openPackageRef()}
            className="href"
            onKeyDown={() => openPackageRef()}
            role="button"
            tabIndex={0}
          >
            View my submitted package.
          </span>
          <br />
          <br />
          <p>Please click the button below to go back home.</p>
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

Success.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    packageRef: PropTypes.string.isRequired,
  }).isRequired,
};
