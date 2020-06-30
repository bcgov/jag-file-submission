import React from "react";
import PropTypes from "prop-types";
import { FaPrint } from "react-icons/fa";

import "./TermsOfUse.css";

export default function TermsOfUse({
  onScroll,
  acceptTerms,
  content,
  heading,
  confirmText
}) {
  return (
    <div>
      <div style={{ width: "100%" }}>
        <span role="button" className="print-page print" tabIndex={0}>
          Print
          <FaPrint style={{ marginLeft: "10px" }} />
        </span>

        <h1>{heading}</h1>
      </div>

      <section className="scroll-box" onScroll={onScroll}>
        {content}
      </section>

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
}

TermsOfUse.propTypes = {
  onScroll: PropTypes.func.isRequired,
  acceptTerms: PropTypes.func.isRequired,
  content: PropTypes.any.isRequired,
  heading: PropTypes.string.isRequired,
  confirmText: PropTypes.string.isRequired
};
