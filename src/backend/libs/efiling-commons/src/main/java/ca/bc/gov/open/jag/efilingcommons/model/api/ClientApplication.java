package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * ClientApplication
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-07-29T12:15:54.198-07:00[America/Los_Angeles]")

public class ClientApplication   {
  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("type")
  private String type;

  public ClientApplication displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public ClientApplication type(String type) {
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

