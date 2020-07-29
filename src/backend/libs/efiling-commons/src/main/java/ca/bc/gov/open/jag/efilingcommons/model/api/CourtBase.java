package ca.bc.gov.open.jag.efilingcommons.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CourtBase   {
  @JsonProperty("location")
  private String location;

  @JsonProperty("level")
  private String level;

  @JsonProperty("courtClass")
  private String courtClass;

  @JsonProperty("division")
  private String division;

  @JsonProperty("fileNumber")
  private String fileNumber;

  @JsonProperty("participatingClass")
  private String participatingClass;

  public CourtBase location(String location) {
    this.location = location;
    return this;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public CourtBase level(String level) {
    this.level = level;
    return this;
  }


  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public CourtBase courtClass(String courtClass) {
    this.courtClass = courtClass;
    return this;
  }

  public String getCourtClass() {
    return courtClass;
  }

  public void setCourtClass(String courtClass) {
    this.courtClass = courtClass;
  }

  public CourtBase division(String division) {
    this.division = division;
    return this;
  }

  public String getDivision() {
    return division;
  }

  public void setDivision(String division) {
    this.division = division;
  }

  public CourtBase fileNumber(String fileNumber) {
    this.fileNumber = fileNumber;
    return this;
  }

  public String getFileNumber() {
    return fileNumber;
  }

  public void setFileNumber(String fileNumber) {
    this.fileNumber = fileNumber;
  }

  public CourtBase participatingClass(String participatingClass) {
    this.participatingClass = participatingClass;
    return this;
  }
  public String getParticipatingClass() {
    return participatingClass;
  }

  public void setParticipatingClass(String participatingClass) {
    this.participatingClass = participatingClass;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CourtBase courtBase = (CourtBase) o;
    return Objects.equals(this.location, courtBase.location) &&
        Objects.equals(this.level, courtBase.level) &&
        Objects.equals(this.courtClass, courtBase.courtClass) &&
        Objects.equals(this.division, courtBase.division) &&
        Objects.equals(this.fileNumber, courtBase.fileNumber) &&
        Objects.equals(this.participatingClass, courtBase.participatingClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, level, courtClass, division, fileNumber, participatingClass);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CourtBase {\n");
    
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    courtClass: ").append(toIndentedString(courtClass)).append("\n");
    sb.append("    division: ").append(toIndentedString(division)).append("\n");
    sb.append("    fileNumber: ").append(toIndentedString(fileNumber)).append("\n");
    sb.append("    participatingClass: ").append(toIndentedString(participatingClass)).append("\n");
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

