package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InitialPackage   {
  @JsonProperty("documents")
  private List<DocumentProperties> documents = null;

  @JsonProperty("court")
  private CourtBase court;

  public InitialPackage documents(List<DocumentProperties> documents) {
    this.documents = documents;
    return this;
  }

  public InitialPackage addDocumentsItem(DocumentProperties documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<DocumentProperties>();
    }
    this.documents.add(documentsItem);
    return this;
  }

  public List<DocumentProperties> getDocuments() {
    return documents;
  }

  public void setDocuments(List<DocumentProperties> documents) {
    this.documents = documents;
  }

  public InitialPackage court(CourtBase court) {
    this.court = court;
    return this;
  }


  public CourtBase getCourt() {
    return court;
  }

  public void setCourt(CourtBase court) {
    this.court = court;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InitialPackage initialPackage = (InitialPackage) o;
    return Objects.equals(this.documents, initialPackage.documents) &&
        Objects.equals(this.court, initialPackage.court);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documents, court);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InitialPackage {\n");
    
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    court: ").append(toIndentedString(court)).append("\n");
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

