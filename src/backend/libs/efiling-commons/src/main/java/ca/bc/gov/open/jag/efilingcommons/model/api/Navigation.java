package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Navigation   {
  @JsonProperty("success")
  private Redirect success;

  @JsonProperty("error")
  private Redirect error;

  @JsonProperty("cancel")
  private Redirect cancel;

  public Navigation success(Redirect success) {
    this.success = success;
    return this;
  }

  public Redirect getSuccess() {
    return success;
  }

  public void setSuccess(Redirect success) {
    this.success = success;
  }

  public Navigation error(Redirect error) {
    this.error = error;
    return this;
  }

  public Redirect getError() {
    return error;
  }

  public void setError(Redirect error) {
    this.error = error;
  }

  public Navigation cancel(Redirect cancel) {
    this.cancel = cancel;
    return this;
  }

  public Redirect getCancel() {
    return cancel;
  }

  public void setCancel(Redirect cancel) {
    this.cancel = cancel;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Navigation navigation = (Navigation) o;
    return Objects.equals(this.success, navigation.success) &&
        Objects.equals(this.error, navigation.error) &&
        Objects.equals(this.cancel, navigation.cancel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, error, cancel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Navigation {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    cancel: ").append(toIndentedString(cancel)).append("\n");
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

