import React from "react";
import BcGovTooltip from "./Tooltip";
import { withdrawTooltipData } from "../../../../modules/test-data/withdrawTooltipData";

export default {
  title: "Tooltip",
  component: BcGovTooltip,
};

export const WithdrawTooltip = () => (
  <div style={{ marginLeft: "45%", marginTop: "20%" }}>
    <span id="tiptarget">
      withdraw
      <BcGovTooltip target="tiptarget" content={withdrawTooltipData} />
    </span>
  </div>
);
