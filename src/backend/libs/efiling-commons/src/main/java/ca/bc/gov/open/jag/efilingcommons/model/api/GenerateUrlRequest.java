package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerateUrlRequest   {
  @JsonProperty("clientApplication")
  private ClientApplication clientApplication;

  @JsonProperty("filingPackage")
  private InitialPackage filingPackage;

  @JsonProperty("navigation")
  private Navigation navigation;

  public GenerateUrlRequest clientApplication(ClientApplication clientApplication) {
    this.clientApplication = clientApplication;
    return this;
  }

  public ClientApplication getClientApplication() {
    return clientApplication;
  }

  public void setClientApplication(ClientApplication clientApplication) {
    this.clientApplication = clientApplication;
  }

  public GenerateUrlRequest filingPackage(InitialPackage filingPackage) {
    this.filingPackage = filingPackage;
    return this;
  }


  public InitialPackage getFilingPackage() {
    return filingPackage;
  }

  public void setFilingPackage(InitialPackage filingPackage) {
    this.filingPackage = filingPackage;
  }

  public GenerateUrlRequest navigation(Navigation navigation) {
    this.navigation = navigation;
    return this;
  }


  public Navigation getNavigation() {
    return navigation;
  }

  public void setNavigation(Navigation navigation) {
    this.navigation = navigation;
  }

}

