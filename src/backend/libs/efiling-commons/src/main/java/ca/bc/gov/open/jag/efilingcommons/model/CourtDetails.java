package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CourtDetails {
    @JsonCreator
    public CourtDetails(
            @JsonProperty("courtId") BigDecimal courtId,
            @JsonProperty("courtDescription") String courtDescription,
            @JsonProperty("classDescription") String classDescription,
            @JsonProperty("levelDescription") String levelDescription) {
        this.courtId = courtId;
        this.courtDescription = courtDescription;
        this.classDescription = classDescription;
        this.levelDescription = levelDescription;
    }


    public BigDecimal getCourtId() {
        return courtId;
    }

    public void setCourtId(BigDecimal courtId) {
        this.courtId = courtId;
    }

    public String getCourtDescription() {
        return courtDescription;
    }

    public void setCourtDescription(String courtDescription) {
        this.courtDescription = courtDescription;
    }

    public String getClassDescription() { return classDescription; }

    public void setClassDescription(String classDescription) { this.classDescription = classDescription; }

    public String getLevelDescription() { return levelDescription;  }

    public void setLevelDescription(String levelDescription) { this.levelDescription = levelDescription; }

    BigDecimal courtId;
    String courtDescription;
    String classDescription;
    String levelDescription;
}
