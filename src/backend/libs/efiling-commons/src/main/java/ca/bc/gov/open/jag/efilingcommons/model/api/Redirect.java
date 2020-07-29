package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Redirect   {
  @JsonProperty("url")
  private String url;

  public Redirect url(String url) {
    this.url = url;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}

