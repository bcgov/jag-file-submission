package ca.bc.gov.open.jag.efilingapi.court.models;

import java.math.BigDecimal;

public class IsValidCourtRequest {

    private BigDecimal courtId;
    private String courtLevel;
    private String courtClassification;
    private String applicationCode;

    public BigDecimal getCourtId() {
        return courtId;
    }

    public String getCourtLevel() {
        return courtLevel;
    }

    public String getCourtClassification() {
        return courtClassification;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public IsValidCourtRequest(Builder builder) {
        this.applicationCode = builder.applicationCode;
        this.courtId = builder.courtId;
        this.courtClassification = builder.courtClassification;
        this.courtLevel = builder.courtLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BigDecimal courtId;
        private String courtLevel;
        private String courtClassification;
        private String applicationCode;

        public Builder courtId(BigDecimal courtId) {
            this.courtId = courtId;
            return this;
        }

        public Builder courtLevel(String courtLevel) {
            this.courtLevel = courtLevel;
            return this;
        }

        public Builder courtClassification(String courtClassification) {
            this.courtClassification = courtClassification;
            return this;
        }

        public Builder applicationCode(String applicationCode) {
            this.applicationCode = applicationCode;
            return this;
        }

        public IsValidCourtRequest create() {
            return new IsValidCourtRequest(this);
        }

    }

}
