package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Court extends CourtBase  {
  @JsonProperty("locationDescription")
  private String locationDescription;

  @JsonProperty("levelDescription")
  private String levelDescription;

  @JsonProperty("classDescription")
  private String classDescription;

  public Court locationDescription(String locationDescription) {
    this.locationDescription = locationDescription;
    return this;
  }


  public String getLocationDescription() {
    return locationDescription;
  }

  public void setLocationDescription(String locationDescription) {
    this.locationDescription = locationDescription;
  }

  public Court levelDescription(String levelDescription) {
    this.levelDescription = levelDescription;
    return this;
  }


  public String getLevelDescription() {
    return levelDescription;
  }

  public void setLevelDescription(String levelDescription) {
    this.levelDescription = levelDescription;
  }

  public Court classDescription(String classDescription) {
    this.classDescription = classDescription;
    return this;
  }


  public String getClassDescription() {
    return classDescription;
  }

  public void setClassDescription(String classDescription) {
    this.classDescription = classDescription;
  }

}

