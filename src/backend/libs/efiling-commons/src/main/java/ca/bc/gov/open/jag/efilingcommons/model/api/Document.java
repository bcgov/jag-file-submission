package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Efiling Document
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-07-29T12:15:54.198-07:00[America/Los_Angeles]")

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Document document = (Document) o;
    return Objects.equals(this.description, document.description) &&
        Objects.equals(this.statutoryFeeAmount, document.statutoryFeeAmount) &&
        Objects.equals(this.mimeType, document.mimeType) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, statutoryFeeAmount, mimeType, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Document {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    statutoryFeeAmount: ").append(toIndentedString(statutoryFeeAmount)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
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

