package ca.bc.gov.open.jag.efilingcommons.model;

public class SubmitPackageRequest {

    private FilingPackage filingPackage;
    private AccountDetails accountDetails;

    public FilingPackage getFilingPackage() {
        return filingPackage;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public SubmitPackageRequest(Builder builder) {
        this.accountDetails = builder.accountDetails;
        this.filingPackage = builder.filingPackage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private FilingPackage filingPackage;
        private AccountDetails accountDetails;

        public Builder filingPackage(FilingPackage filingPackage) {
            this.filingPackage = filingPackage;
            return this;
        }

        public Builder accountDetails(AccountDetails accountDetails) {
            this.accountDetails = accountDetails;
            return this;
        }

        public SubmitPackageRequest create() {
            return new SubmitPackageRequest(this);
        }
    }

}
