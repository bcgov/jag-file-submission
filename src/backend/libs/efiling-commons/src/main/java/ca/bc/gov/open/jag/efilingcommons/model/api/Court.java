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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Court court = (Court) o;
    return Objects.equals(this.locationDescription, court.locationDescription) &&
        Objects.equals(this.levelDescription, court.levelDescription) &&
        Objects.equals(this.classDescription, court.classDescription) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationDescription, levelDescription, classDescription, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Court {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    locationDescription: ").append(toIndentedString(locationDescription)).append("\n");
    sb.append("    levelDescription: ").append(toIndentedString(levelDescription)).append("\n");
    sb.append("    classDescription: ").append(toIndentedString(classDescription)).append("\n");
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

