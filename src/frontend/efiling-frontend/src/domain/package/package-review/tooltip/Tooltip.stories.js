/* eslint-disable no-var */
/* eslint-disable import/no-mutable-exports */
/* eslint-disable vars-on-top */
import React from "react";
import BcGovTooltip from "./Tooltip";
import { withdrawTooltipData } from "../../../../modules/test-data/withdrawTooltipData";

export default {
  title: "Tooltip",
  component: BcGovTooltip,
};

export var WithdrawTooltip = function () {
  return (
    <div style={{ marginLeft: "45%", marginTop: "20%" }}>
      <span id="tiptarget">
        withdraw
        <BcGovTooltip target="tiptarget" content={withdrawTooltipData} />
      </span>
    </div>
  );
};
