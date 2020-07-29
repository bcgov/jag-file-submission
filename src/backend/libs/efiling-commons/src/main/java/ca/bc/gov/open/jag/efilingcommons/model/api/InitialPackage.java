package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

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
      this.documents = new ArrayList<>();
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
}

