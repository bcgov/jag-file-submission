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
}

