package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingapi.api.model.ClientApplication;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.Navigation;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Represents the submission details for a transaction
 */
public class Submission {

    private UUID id;

    private UUID transactionId;

    private UUID universalId;

    private long expiryDate;

    private AccountDetails accountDetails;

    private Navigation navigation;

    private ClientApplication clientApplication;

    private FilingPackage filingPackage;

    private boolean rushedSubmission;

    protected Submission(Submission.Builder builder) {
        this.id = builder.id;
        this.transactionId = builder.transactionId;
        this.accountDetails = builder.accountDetails;
        this.filingPackage = builder.filingPackage;
        this.navigation = builder.navigation;
        this.clientApplication = builder.clientApplication;
        this.expiryDate = builder.expiryDate;
        this.universalId = builder.universalId;
        this.rushedSubmission = builder.rushedSubmission;
    }

    public static Submission.Builder builder() {
        return new Submission.Builder();
    }

    @JsonCreator
    public Submission(
            @JsonProperty("id") UUID id,
            @JsonProperty("owner") UUID transactionId,
            @JsonProperty("bceid") UUID universalId,
            @JsonProperty("accountDetails") AccountDetails accountDetails,
            @JsonProperty("package") FilingPackage filingPackage,
            @JsonProperty("navigation") Navigation navigation,
            @JsonProperty("clientApplication") ClientApplication clientApplication,
            @JsonProperty("expiryDate") long expiryDate,
            @JsonProperty("rushedSubmission") boolean rushedSubmission) {
        this.id = id;
        this.transactionId = transactionId;
        this.universalId = universalId;
        this.accountDetails = accountDetails;
        this.filingPackage = filingPackage;
        this.navigation = navigation;
        this.clientApplication = clientApplication;
        this.expiryDate = expiryDate;
        this.rushedSubmission = rushedSubmission;
    }

    public UUID getId() { return id; }

    public UUID getTransactionId() { return transactionId; }

    public UUID getUniversalId() { return universalId; }

    public FilingPackage getFilingPackage() {
        return filingPackage;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public ClientApplication getClientApplication() { return clientApplication; }

    public long getExpiryDate() {
        return expiryDate;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public boolean isRushedSubmission() { return rushedSubmission; }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public static class Builder {

        private UUID id;
        private UUID transactionId;
        private UUID universalId;
        private AccountDetails accountDetails;
        private FilingPackage filingPackage;
        private Navigation navigation;
        private ClientApplication clientApplication;
        private long expiryDate;
        private boolean rushedSubmission;

        public Builder id (UUID id) {
            this.id = id;
            return this;
        }

        public Builder accountDetails (AccountDetails accountDetails) {
            this.accountDetails = accountDetails;
            return this;
        }

        public Builder transactionId(UUID owner) {
            this.transactionId = owner;
            return this;
        }

        public Builder universalId(UUID universalId) {
            this.universalId = universalId;
            return this;
        }

        public Builder filingPackage(FilingPackage filingPackage) {
            this.filingPackage =  filingPackage;
            return this;
        }

        public Builder navigation(Navigation navigation) {
            this.navigation = navigation;
            return this;
        }

        public Builder clientApplication(ClientApplication clientApplication) {
            this.clientApplication = clientApplication;
            return this;
        }

        public Builder expiryDate(long expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder rushedSubmission(boolean rushedSubmission) {
            this.rushedSubmission = rushedSubmission;
            return this;
        }

        public Submission create() {
            return new Submission(this);
        }
    }

}
