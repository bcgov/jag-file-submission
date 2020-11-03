package ca.bc.gov.open.jag.efilingapi.court.models;

import java.math.BigDecimal;

public class IsValidCourtRequestRequest {

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

    public IsValidCourtRequestRequest(Builder builder) {
        this.courtId = builder.courtId;
        this.courtLevel = builder.courtLevel;
        this.courtClassification = builder.courtClassification;
        this.applicationCode = builder.applicationCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BigDecimal courtId;
        private String applicationCode;
        private String courtClassification;
        private String courtLevel;

        public Builder courtId(BigDecimal courtId) {
            this.courtId = courtId;
            return this;
        }

        public Builder courtClassification(String courtClassification) {
            this.courtClassification = courtClassification;
            return this;
        }

        public Builder courtLevel(String courtLevel) {
            this.courtLevel = courtLevel;
            return this;
        }

        public Builder applicationCode(String applicationCode) {
            this.applicationCode = applicationCode;
            return this;
        }

        public IsValidCourtRequestRequest create() {
            return new IsValidCourtRequestRequest(this);
        }

    }


}
