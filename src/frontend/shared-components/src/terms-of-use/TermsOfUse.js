import React from "react";
import PropTypes from "prop-types";
import { FaPrint } from "react-icons/fa";
import { Button } from "../button/Button";

import "./TermsOfUse.css";

export default function TermsOfUse({
  onScroll,
  acceptTerms,
  continueButton,
  cancelButton,
  content
}) {
  return (
    <div>
      <div style={{ width: "100%" }}>
        <span role="button" className="print-page print" tabIndex={0}>
          Print
          <FaPrint style={{ marginLeft: "10px" }} />
        </span>

        <h1>Terms of Use</h1>
      </div>

      <section className="scroll-box" onScroll={onScroll}>
        {content}
      </section>

      <section className="pt-2">
        <label htmlFor="acceptTerms">
          <input id="acceptTerms" type="checkbox" onClick={acceptTerms} />{" "}
          <b>I have read and accept the above terms of use</b>
          <span id="asterisk" className="mandatory">
            *
          </span>
        </label>
      </section>

      <section className="buttons pt-4">
        <Button
          label={cancelButton.label}
          onClick={cancelButton.onClick}
          styling={cancelButton.styling}
        />
        <Button
          label={continueButton.label}
          onClick={continueButton.onClick}
          styling={continueButton.styling}
        />
      </section>
    </div>
  );
}
