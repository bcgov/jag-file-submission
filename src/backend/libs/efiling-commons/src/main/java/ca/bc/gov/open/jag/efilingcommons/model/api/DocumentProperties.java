package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DocumentProperties   {
  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  public DocumentProperties name(String name) {
    this.name = name;
    return this;
  }
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DocumentProperties type(String type) {
    this.type = type;
    return this;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}

