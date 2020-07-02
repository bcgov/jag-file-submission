import React from "react";
import PropTypes from "prop-types";
import { FaPrint } from "react-icons/fa";

import "./TermsOfUse.css";

export const TermsOfUse = ({ acceptTerms, content, heading, confirmText }) => {
  return (
    <div>
      <div style={{ width: "100%" }}>
        <span role="button" className="print-page print" tabIndex={0}>
          Print
          <FaPrint style={{ marginLeft: "10px" }} />
        </span>

        <h3>{heading}</h3>
      </div>

      <section className="scroll-box">{content}</section>

      <section className="pt-2">
        <label htmlFor="acceptTerms">
          <input id="acceptTerms" type="checkbox" onClick={acceptTerms} />
          &nbsp;
          <b>{confirmText}</b>
          <span id="asterisk" className="mandatory">
            *
          </span>
        </label>
      </section>
    </div>
  );
};

TermsOfUse.propTypes = {
  acceptTerms: PropTypes.func.isRequired,
  content: PropTypes.element.isRequired,
  heading: PropTypes.string.isRequired,
  confirmText: PropTypes.string.isRequired
};
