package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CourtDetails {
    @JsonCreator
    public CourtDetails(
            @JsonProperty("courtId") BigDecimal courtId,
            @JsonProperty("courtDescription") String courtDescription) {
        this.courtId = courtId;
        this.courtDescription = courtDescription;
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

    BigDecimal courtId;
    String courtDescription;
}
