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

}

