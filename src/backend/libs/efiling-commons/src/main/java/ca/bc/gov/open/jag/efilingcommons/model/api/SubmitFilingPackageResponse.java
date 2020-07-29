package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Objects;


public class SubmitFilingPackageResponse   {
  @JsonProperty("transactionId")
  private BigDecimal transactionId;

  @JsonProperty("acknowledge")
  private LocalDate acknowledge = null;

  public SubmitFilingPackageResponse transactionId(BigDecimal transactionId) {
    this.transactionId = transactionId;
    return this;
  }


  public BigDecimal getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(BigDecimal transactionId) {
    this.transactionId = transactionId;
  }

  public SubmitFilingPackageResponse acknowledge(LocalDate acknowledge) {
    this.acknowledge = acknowledge;
    return this;
  }

  public LocalDate getAcknowledge() {
    return acknowledge;
  }

  public void setAcknowledge(LocalDate acknowledge) {
    this.acknowledge = acknowledge;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmitFilingPackageResponse submitFilingPackageResponse = (SubmitFilingPackageResponse) o;
    return Objects.equals(this.transactionId, submitFilingPackageResponse.transactionId) &&
        Objects.equals(this.acknowledge, submitFilingPackageResponse.acknowledge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, acknowledge);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmitFilingPackageResponse {\n");
    
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    acknowledge: ").append(toIndentedString(acknowledge)).append("\n");
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

