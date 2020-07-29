package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * GetSubmissionResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-07-29T12:15:54.198-07:00[America/Los_Angeles]")

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSubmissionResponse getSubmissionResponse = (GetSubmissionResponse) o;
    return Objects.equals(this.userDetails, getSubmissionResponse.userDetails) &&
        Objects.equals(this.navigation, getSubmissionResponse.navigation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userDetails, navigation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSubmissionResponse {\n");
    
    sb.append("    userDetails: ").append(toIndentedString(userDetails)).append("\n");
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

