package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Document extends DocumentProperties  {
  @JsonProperty("description")
  private String description;

  @JsonProperty("statutoryFeeAmount")
  private BigDecimal statutoryFeeAmount;

  @JsonProperty("mimeType")
  private String mimeType;

  public Document description(String description) {
    this.description = description;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Document statutoryFeeAmount(BigDecimal statutoryFeeAmount) {
    this.statutoryFeeAmount = statutoryFeeAmount;
    return this;
  }

  public BigDecimal getStatutoryFeeAmount() {
    return statutoryFeeAmount;
  }

  public void setStatutoryFeeAmount(BigDecimal statutoryFeeAmount) {
    this.statutoryFeeAmount = statutoryFeeAmount;
  }

  public Document mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }


  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

}

