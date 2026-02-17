package ca.bc.gov.open.jag.efilingapi.court.models;

import java.math.BigDecimal;

public class IsValidCourtFileNumberRequest {

    private String fileNumber;
    private BigDecimal courtId;
    private String courtLevel;
    private String courtClassification;
    private String applicationCode;

    public String getFileNumber() {
        return fileNumber;
    }

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

    public IsValidCourtFileNumberRequest(Builder builder) {
        this.fileNumber = builder.fileNumber;
        this.courtId = builder.courtId;
        this.courtLevel = builder.courtLevel;
        this.courtClassification = builder.courtClassification;
        this.applicationCode = builder.applicationCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String fileNumber;
        private BigDecimal courtId;
        private String courtLevel;
        private String courtClassification;
        private String applicationCode;

        public Builder applicationCode(String applicationCode) {
            this.applicationCode = applicationCode;
            return this;
        }

        public Builder fileNumber(String fileNumber) {
            this.fileNumber = fileNumber;
            return this;
        }

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

        public IsValidCourtFileNumberRequest create() {
            return new IsValidCourtFileNumberRequest(this);
        }

    }

}
