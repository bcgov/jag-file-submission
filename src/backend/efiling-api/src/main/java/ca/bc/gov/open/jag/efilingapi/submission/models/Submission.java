package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingapi.api.model.NavigationUrls;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Represents the submission details for a transaction
 */
public class Submission {

    private UUID id;

    private UUID transactionId;

    private String universalId;

    private long expiryDate;

    private NavigationUrls navigationUrls;

    private FilingPackage filingPackage;

    private String clientAppName;

    protected Submission(Submission.Builder builder) {
        this.id = builder.id;
        this.transactionId = builder.transactionId;
        this.filingPackage = builder.filingPackage;
        this.navigationUrls = builder.navigationUrls;
        this.expiryDate = builder.expiryDate;
        this.universalId = builder.universalId;
        this.clientAppName = builder.clientAppName;
    }

    public static Submission.Builder builder() {
        return new Submission.Builder();
    }

    @JsonCreator
    public Submission(
            @JsonProperty("id") UUID id,
            @JsonProperty("transactionId") UUID transactionId,
            @JsonProperty("universalId") String universalId,
            @JsonProperty("clientAppName") String clientAppName,
            @JsonProperty("filingPackage") FilingPackage filingPackage,
            @JsonProperty("navigationUrls") NavigationUrls navigationUrls,
            @JsonProperty("expiryDate") long expiryDate) {
        this.id = id;
        this.transactionId = transactionId;
        this.universalId = universalId;
        this.filingPackage = filingPackage;
        this.navigationUrls = navigationUrls;
        this.expiryDate = expiryDate;
        this.clientAppName = clientAppName;
    }

    public UUID getId() { return id; }

    public UUID getTransactionId() { return transactionId; }

    public String getUniversalId() { return universalId; }

    public String getClientAppName() { return clientAppName; }

    public FilingPackage getFilingPackage() {
        return filingPackage;
    }

    public NavigationUrls getNavigationUrls() { return navigationUrls; }

    public long getExpiryDate() {
        return expiryDate;
    }
    
    public static class Builder {

        private UUID id;
        private UUID transactionId;
        private String universalId;
        private FilingPackage filingPackage;
        private NavigationUrls navigationUrls;
        private long expiryDate;
        private String clientAppName;


        public Builder id (UUID id) {
            this.id = id;
            return this;
        }

        public Builder clientAppName(String clientAppName) {
            this.clientAppName = clientAppName;
            return this;
        }


        public Builder transactionId(UUID owner) {
            this.transactionId = owner;
            return this;
        }

        public Builder universalId(String universalId) {
            this.universalId = universalId;
            return this;
        }

        public Builder filingPackage(FilingPackage filingPackage) {
            this.filingPackage =  filingPackage;
            return this;
        }

        public Builder navigationUrls(NavigationUrls navigationUrls) {
            this.navigationUrls = navigationUrls;
            return this;
        }

        public Builder expiryDate(long expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Submission create() {
            return new Submission(this);
        }
    }

}
