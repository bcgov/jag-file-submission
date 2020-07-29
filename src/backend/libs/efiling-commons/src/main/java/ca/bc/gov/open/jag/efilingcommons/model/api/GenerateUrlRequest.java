package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateUrlRequest generateUrlRequest = (GenerateUrlRequest) o;
    return Objects.equals(this.clientApplication, generateUrlRequest.clientApplication) &&
        Objects.equals(this.filingPackage, generateUrlRequest.filingPackage) &&
        Objects.equals(this.navigation, generateUrlRequest.navigation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientApplication, filingPackage, navigation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateUrlRequest {\n");
    
    sb.append("    clientApplication: ").append(toIndentedString(clientApplication)).append("\n");
    sb.append("    filingPackage: ").append(toIndentedString(filingPackage)).append("\n");
    sb.append("    navigation: ").append(toIndentedString(navigation)).append("\n");
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

