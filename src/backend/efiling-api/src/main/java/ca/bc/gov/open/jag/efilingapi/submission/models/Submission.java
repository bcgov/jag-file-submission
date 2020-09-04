package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingapi.api.model.NavigationUrls;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
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

    private NavigationUrls navigationUrls;

    private FilingPackage filingPackage;

    private boolean rushedSubmission;

    private String clientAppName;

    protected Submission(Submission.Builder builder) {
        this.id = builder.id;
        this.transactionId = builder.transactionId;
        this.accountDetails = builder.accountDetails;
        this.filingPackage = builder.filingPackage;
        this.navigationUrls = builder.navigation;
        this.expiryDate = builder.expiryDate;
        this.universalId = builder.universalId;
        this.rushedSubmission = builder.rushedSubmission;
        this.clientAppName = builder.clientAppName;
    }

    public static Submission.Builder builder() {
        return new Submission.Builder();
    }

    @JsonCreator
    public Submission(
            @JsonProperty("id") UUID id,
            @JsonProperty("transactionId") UUID transactionId,
            @JsonProperty("universalId") UUID universalId,
            @JsonProperty("clientAppName") String clientAppName,
            @JsonProperty("accountDetails") AccountDetails accountDetails,
            @JsonProperty("filingPackage") FilingPackage filingPackage,
            @JsonProperty("navigationUrls") NavigationUrls navigationUrls,
            @JsonProperty("expiryDate") long expiryDate,
            @JsonProperty("rushedSubmission") boolean rushedSubmission) {
        this.id = id;
        this.transactionId = transactionId;
        this.universalId = universalId;
        this.accountDetails = accountDetails;
        this.filingPackage = filingPackage;
        this.navigationUrls = navigationUrls;
        this.expiryDate = expiryDate;
        this.rushedSubmission = rushedSubmission;
    }

    public UUID getId() { return id; }

    public UUID getTransactionId() { return transactionId; }

    public UUID getUniversalId() { return universalId; }

    public String getClientAppName() { return clientAppName; }

    public FilingPackage getFilingPackage() {
        return filingPackage;
    }

    public NavigationUrls getNavigationUrls() { return navigationUrls; }

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
        private NavigationUrls navigation;
        private long expiryDate;
        private boolean rushedSubmission;
        private String clientAppName;


        public Builder id (UUID id) {
            this.id = id;
            return this;
        }

        public Builder clientAppName(String clientAppName) {
            this.clientAppName = clientAppName;
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

        public Builder navigationUrls(NavigationUrls navigationUrls) {
            this.navigation = navigationUrls;
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
