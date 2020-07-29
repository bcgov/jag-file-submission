package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FilingPackage   {
  @JsonProperty("submissionFeeAmount")
  private BigDecimal submissionFeeAmount;

  @JsonProperty("court")
  private Court court;

  @JsonProperty("documents")
  private List<Document> documents = new ArrayList<Document>();

  public FilingPackage submissionFeeAmount(BigDecimal submissionFeeAmount) {
    this.submissionFeeAmount = submissionFeeAmount;
    return this;
  }

  public BigDecimal getSubmissionFeeAmount() {
    return submissionFeeAmount;
  }

  public void setSubmissionFeeAmount(BigDecimal submissionFeeAmount) {
    this.submissionFeeAmount = submissionFeeAmount;
  }

  public FilingPackage court(Court court) {
    this.court = court;
    return this;
  }

  public Court getCourt() {
    return court;
  }

  public void setCourt(Court court) {
    this.court = court;
  }

  public FilingPackage documents(List<Document> documents) {
    this.documents = documents;
    return this;
  }

  public FilingPackage addDocumentsItem(Document documentsItem) {
    this.documents.add(documentsItem);
    return this;
  }

  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

}

