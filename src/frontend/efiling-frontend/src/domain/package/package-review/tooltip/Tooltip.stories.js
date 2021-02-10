import React from "react";
import BcGovTooltip from "./Tooltip";

export default {
  title: "Tooltip",
  component: BcGovTooltip,
};

const content = (
  <>
    <p>
      The withdraw link will change the status of the document from submitted to
      withdrawn and the document will not be processed by court registry staff.
    </p>
    <br />
    <p>
      <b>Note: </b>
      If there are multiple documents in your package, you would need to
      withdraw each document to remove the package from the court registry
      processing queue
    </p>
  </>
);

export const WithdrawTooltip = () => (
  <div style={{ marginLeft: "45%", marginTop: "20%" }}>
    <span id="tiptarget">
      withdraw
      <BcGovTooltip target="tiptarget" content={content} />
    </span>
  </div>
);
