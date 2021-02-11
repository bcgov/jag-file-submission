import React from "react";
import PropTypes from "prop-types";
import { UncontrolledTooltip } from "reactstrap";
import "./Tooltip.scoped.css";
import "./Tooltip.css";

export default function BcGovTooltip({ content, styling, target }) {
  return (
    <div className={`bcgov-withdraw-tooltip ${styling}`}>
      <UncontrolledTooltip placement="right" target={target}>
        {content}
      </UncontrolledTooltip>
    </div>
  );
}

BcGovTooltip.propTypes = {
  content: PropTypes.element.isRequired,
  target: PropTypes.string.isRequired,
  styling: PropTypes.string,
};

BcGovTooltip.defaultProps = {
  styling: "",
};
