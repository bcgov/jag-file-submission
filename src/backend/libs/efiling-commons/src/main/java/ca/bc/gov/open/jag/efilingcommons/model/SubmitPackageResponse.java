package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigDecimal;

public class SubmitPackageResponse {

    private String packageLink;
    private BigDecimal transactionId;

    public SubmitPackageResponse(Builder builder) {
        this.packageLink = builder.packageLink;
        this.transactionId = builder.transactionId;
    }

    public String getPackageLink() {
        return packageLink;
    }

    public BigDecimal getTransactionId() {
        return transactionId;
    }

    public static Builder builder() {
        return new SubmitPackageResponse.Builder();
    }

    public static class Builder {

        private String packageLink;
        private BigDecimal transactionId;

        public Builder packageLink(String packageLink) { this.packageLink = packageLink; return this;}
        public Builder transactionId(BigDecimal transactionId) { this.transactionId = transactionId; return this;}

        public SubmitPackageResponse create() {
            return new SubmitPackageResponse(this);
        }

    }

}
