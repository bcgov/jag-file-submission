package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigInteger;

public class SubmitPackageResponse {

    private String packageLink;
    private BigInteger transactionId;

    public SubmitPackageResponse(Builder builder) {
        this.packageLink = builder.packageLink;
        this.transactionId = builder.transactionId;
    }

    public static class Builder {

        private String packageLink;
        private BigInteger transactionId;

        public Builder packageLink(String packageLink) { this.packageLink = packageLink; return this;}
        public Builder transactionId(BigInteger transactionId) { this.transactionId = transactionId; return this;}

        public SubmitPackageResponse create() {
            return new SubmitPackageResponse(this);
        }

    }

}
