package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetSubmissionResponse   {
  @JsonProperty("userDetails")
  private UserDetails userDetails;

  @JsonProperty("navigation")
  private Navigation navigation;

  public GetSubmissionResponse userDetails(UserDetails userDetails) {
    this.userDetails = userDetails;
    return this;
  }

  public UserDetails getUserDetails() {
    return userDetails;
  }

  public void setUserDetails(UserDetails userDetails) {
    this.userDetails = userDetails;
  }

  public GetSubmissionResponse navigation(Navigation navigation) {
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

