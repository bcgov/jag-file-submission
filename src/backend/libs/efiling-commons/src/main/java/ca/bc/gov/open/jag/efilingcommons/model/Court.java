package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Court {

    private BigDecimal agencyId;
    private String locationDescription;
    private String location;
    private String level;
    private String levelDescription;
    private String courtClass;
    private String classDescription;
    private String division;
    private String fileNumber;
    private String participatingClass;


    public Court(Builder builder) {
        this.agencyId = builder.agencyId;
        this.location = builder.location;
        this.locationDescription = builder.locationDescription;
        this.level = builder.level;
        this.levelDescription = builder.levelDescription;
        this.courtClass = builder.courtClass;
        this.classDescription = builder.classDescription;
        this.division = builder.division;
        this.fileNumber = builder.fileNumber;
        this.participatingClass = builder.participatingClass;
    }

    @JsonCreator
    public Court(
            @JsonProperty("agencyId") BigDecimal agencyId,
            @JsonProperty("location") String location,
            @JsonProperty("locationDescription") String locationDescription,
            @JsonProperty("level") String level,
            @JsonProperty("levelDescription") String levelDescription,
            @JsonProperty("courtClass") String courtClass,
            @JsonProperty("classDescription") String classDescription,
            @JsonProperty("division") String division,
            @JsonProperty("fileNumber") String fileNumber,
            @JsonProperty("participatingClass") String participatingClass) {
        this.agencyId = agencyId;
        this.location = location;
        this.locationDescription = locationDescription;
        this.level = level;
        this.levelDescription = levelDescription;
        this.courtClass = courtClass;
        this.classDescription = classDescription;
        this.division = division;
        this.fileNumber = fileNumber;
        this.participatingClass = participatingClass;
    }

    public String getLocation() {
        return location;
    }

    public String getLevel() {
        return level;
    }

    public String getCourtClass() {
        return courtClass;
    }

    public String getDivision() {
        return division;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public String getParticipatingClass() {
        return participatingClass;
    }

    public BigDecimal getAgencyId() {
        return agencyId;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BigDecimal agencyId;
        private String location;
        private String locationDescription;
        private String level;
        private String levelDescription;
        private String courtClass;
        private String classDescription;
        private String division;
        private String fileNumber;
        private String participatingClass;

        public Builder agencyId(BigDecimal agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder locationDescription(String locationDescription) {
            this.locationDescription = locationDescription;
            return this;
        }

        public Builder level(String level) {
            this.level = level;
            return this;
        }

        public Builder levelDescription(String levelDescription) {
            this.levelDescription = levelDescription;
            return this;
        }

        public Builder courtClass(String courtClass) {
            this.courtClass = courtClass;
            return this;
        }

        public Builder classDescription(String classDescription) {
            this.classDescription = classDescription;
            return this;
        }

        public Builder division(String division) {
            this.division = division;
            return this;
        }

        public Builder fileNumber(String fileNumber) {
            this.fileNumber = fileNumber;
            return this;
        }

        public Builder participatingClass(String participatingClass) {
            this.participatingClass = participatingClass;
            return this;
        }

        public Court create() {
            return new Court(this);
        }

    }

}
