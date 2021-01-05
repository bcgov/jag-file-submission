package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import java.math.BigDecimal;

public class ReviewCourt {
    private String locationDescription;
    private String locationCd;
    private String locationName;
    private String level;
    private String levelDescription;
    private String courtClass;
    private String classDescription;
    private String division;
    private String fileNumber;
    private String participatingClass;
    private BigDecimal locationId;
    private Boolean existingFileYN;

    public ReviewCourt() {

    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public void setLevelDescription(String levelDescription) {
        this.levelDescription = levelDescription;
    }

    public String getCourtClass() {
        return courtClass;
    }

    public void setCourtClass(String courtClass) {
        this.courtClass = courtClass;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getParticipatingClass() {
        return participatingClass;
    }

    public void setParticipatingClass(String participatingClass) {
        this.participatingClass = participatingClass;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public Boolean getExistingFileYN() {
        return existingFileYN;
    }

    public void setExistingFileYN(Boolean existingFileYN) {
        this.existingFileYN = existingFileYN;
    }

    public String getLocationCd() {
        return locationCd;
    }

    public void setLocationCd(String locationCd) {
        this.locationCd = locationCd;
    }
}
