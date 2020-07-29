package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class UploadSubmissionDocumentsResponse   {
  @JsonProperty("submissionId")
  private UUID submissionId;

  @JsonProperty("received")
  private BigDecimal received;

  public UploadSubmissionDocumentsResponse submissionId(UUID submissionId) {
    this.submissionId = submissionId;
    return this;
  }

  public UUID getSubmissionId() {
    return submissionId;
  }

  public void setSubmissionId(UUID submissionId) {
    this.submissionId = submissionId;
  }

  public UploadSubmissionDocumentsResponse received(BigDecimal received) {
    this.received = received;
    return this;
  }


  public BigDecimal getReceived() {
    return received;
  }

  public void setReceived(BigDecimal received) {
    this.received = received;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadSubmissionDocumentsResponse uploadSubmissionDocumentsResponse = (UploadSubmissionDocumentsResponse) o;
    return Objects.equals(this.submissionId, uploadSubmissionDocumentsResponse.submissionId) &&
        Objects.equals(this.received, uploadSubmissionDocumentsResponse.received);
  }

  @Override
  public int hashCode() {
    return Objects.hash(submissionId, received);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadSubmissionDocumentsResponse {\n");
    
    sb.append("    submissionId: ").append(toIndentedString(submissionId)).append("\n");
    sb.append("    received: ").append(toIndentedString(received)).append("\n");
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

