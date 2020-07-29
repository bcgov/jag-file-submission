package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmitFilingPackageRequest submitFilingPackageRequest = (SubmitFilingPackageRequest) o;
    return Objects.equals(this.isRushOrder, submitFilingPackageRequest.isRushOrder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isRushOrder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmitFilingPackageRequest {\n");
    
    sb.append("    isRushOrder: ").append(toIndentedString(isRushOrder)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

