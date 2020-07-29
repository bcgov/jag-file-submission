package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateUrlResponse generateUrlResponse = (GenerateUrlResponse) o;
    return Objects.equals(this.expiryDate, generateUrlResponse.expiryDate) &&
        Objects.equals(this.efilingUrl, generateUrlResponse.efilingUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expiryDate, efilingUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateUrlResponse {\n");
    
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    efilingUrl: ").append(toIndentedString(efilingUrl)).append("\n");
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

