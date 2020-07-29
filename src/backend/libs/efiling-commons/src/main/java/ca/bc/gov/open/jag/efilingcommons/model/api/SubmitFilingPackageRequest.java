package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitFilingPackageRequest   {
  @JsonProperty("isRushOrder")
  private Boolean isRushOrder;

  public SubmitFilingPackageRequest isRushOrder(Boolean isRushOrder) {
    this.isRushOrder = isRushOrder;
    return this;
  }

  public Boolean getIsRushOrder() {
    return isRushOrder;
  }

  public void setIsRushOrder(Boolean isRushOrder) {
    this.isRushOrder = isRushOrder;
  }
}

