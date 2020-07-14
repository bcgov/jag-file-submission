package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingapi.api.model.ClientApplication;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.Navigation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Represents the submission details for a transaction
 */
public class Submission {

    private UUID id;

    private long expiryDate;

    private DocumentProperties documentProperties;

    private Navigation navigation;

    private ClientApplication clientApplication;

    private Fee fee;

    private AccountDetails accountDetails;

    protected Submission(Submission.Builder builder) {
        this.id = UUID.randomUUID();
        this.documentProperties = builder.documentProperties;
        this.navigation = builder.navigation;
        this.clientApplication = builder.clientApplication;
        this.fee = builder.fee;
        this.accountDetails = builder.accountDetails;
        this.expiryDate = builder.expiryDate;
    }

    public static Submission.Builder builder() {
        return new Submission.Builder();
    }

    @JsonCreator
    public Submission(
            @JsonProperty("id") UUID id,
            @JsonProperty("submissionMetadata") DocumentProperties documentProperties,
            @JsonProperty("navigation") Navigation navigation,
            @JsonProperty("clientApplication") ClientApplication clientApplication,
            @JsonProperty("fee") Fee fee,
            @JsonProperty("accountDetails") AccountDetails accountDetails,
            @JsonProperty("expiryDate") long expiryDate) {
        this.id = id;
        this.documentProperties = documentProperties;
        this.navigation = navigation;
        this.clientApplication = clientApplication;
        this.fee = fee;
        this.accountDetails = accountDetails;
        this.expiryDate = expiryDate;
    }

    public UUID getId() { return id; }

    public DocumentProperties getDocumentProperties() {
        return documentProperties;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public ClientApplication getClientApplication() { return clientApplication; }

    public Fee getFee() {
        return fee;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public static class Builder {

        private DocumentProperties documentProperties;
        private Navigation navigation;
        private ClientApplication clientApplication;
        private Fee fee;
        private AccountDetails accountDetails;
        private long expiryDate;

        public Builder documentProperties(DocumentProperties documentProperties) {
            this.documentProperties =  documentProperties;
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

        public Builder fee(Fee fee) {
            this.fee = fee;
            return this;
        }

        public Builder accountDetails(AccountDetails accountDetails) {
            this.accountDetails = accountDetails;
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
