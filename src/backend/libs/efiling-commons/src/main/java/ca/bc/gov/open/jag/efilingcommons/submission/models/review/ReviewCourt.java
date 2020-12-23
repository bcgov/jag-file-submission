package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import ca.bc.gov.open.jag.efilingcommons.model.Court;

import java.math.BigDecimal;

public class ReviewCourt extends Court {
    private BigDecimal locationId;
    private Boolean existingFileYN;
    public ReviewCourt(Builder builder) {
        super(builder);
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
}
