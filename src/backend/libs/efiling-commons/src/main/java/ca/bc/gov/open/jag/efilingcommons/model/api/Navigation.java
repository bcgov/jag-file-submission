package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}

