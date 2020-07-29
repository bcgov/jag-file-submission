package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilingPackage filingPackage = (FilingPackage) o;
    return Objects.equals(this.submissionFeeAmount, filingPackage.submissionFeeAmount) &&
        Objects.equals(this.court, filingPackage.court) &&
        Objects.equals(this.documents, filingPackage.documents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(submissionFeeAmount, court, documents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilingPackage {\n");
    
    sb.append("    submissionFeeAmount: ").append(toIndentedString(submissionFeeAmount)).append("\n");
    sb.append("    court: ").append(toIndentedString(court)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
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

