package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

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

}

