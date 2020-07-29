package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerateUrlResponse   {
  @JsonProperty("expiryDate")
  private Long expiryDate;

  @JsonProperty("efilingUrl")
  private String efilingUrl;

  public GenerateUrlResponse expiryDate(Long expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  public Long getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Long expiryDate) {
    this.expiryDate = expiryDate;
  }

  public GenerateUrlResponse efilingUrl(String efilingUrl) {
    this.efilingUrl = efilingUrl;
    return this;
  }

  public String getEfilingUrl() {
    return efilingUrl;
  }

  public void setEfilingUrl(String efilingUrl) {
    this.efilingUrl = efilingUrl;
  }

}

