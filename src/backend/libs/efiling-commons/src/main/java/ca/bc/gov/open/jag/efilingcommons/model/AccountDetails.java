package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AccountDetails {

    private String universalId;
    private BigDecimal accountId;
    private BigDecimal clientId;
    private String internalClientNumber;
    private boolean fileRolePresent;
    private boolean cardRegistered;

    protected AccountDetails(AccountDetails.Builder builder) {

        this.universalId = builder.universalId;
        this.accountId = builder.accountId;
        this.clientId = builder.clientId;
        this.fileRolePresent = builder.fileRolePresent;
        this.cardRegistered = builder.cardRegistered;
        this.internalClientNumber = builder.internalClientNumber;

    }

    public static AccountDetails.Builder builder() {

        return new AccountDetails.Builder();
    }

    @JsonCreator
    public AccountDetails(
            @JsonProperty("universalId") String universalId,
            @JsonProperty("accountId") BigDecimal accountId,
            @JsonProperty("clientId") BigDecimal clientId,
            @JsonProperty("internalClientNumber") String internalClientNumber,
            @JsonProperty("fileRolePresent") boolean fileRolePresent,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("middleName") String middleName,
            @JsonProperty("email") String email,
            @JsonProperty("cardRegistered") Boolean cardRegistered) {
        this.universalId = universalId;
        this.accountId = accountId;
        this.clientId = clientId;
        this.internalClientNumber = internalClientNumber;
        this.fileRolePresent = fileRolePresent;
        this.cardRegistered = cardRegistered;
    }

    public String getUniversalId() {
        return universalId;
    }

    public BigDecimal getAccountId() {
        return accountId;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public String getInternalClientNumber() { return internalClientNumber; }

    public boolean isFileRolePresent() {
        return fileRolePresent;
    }

    public boolean isCardRegistered() { return cardRegistered; }

    public void updateInternalClientNumber(String internalClientNumber) {
        this.internalClientNumber = internalClientNumber;
        this.cardRegistered = this.internalClientNumber != null;
    }

    public static class Builder {

        private String universalId;
        private BigDecimal accountId;
        private BigDecimal clientId;
        private String internalClientNumber;
        private boolean fileRolePresent;
        private boolean cardRegistered;


        public Builder universalId(String universalId) {
            this.universalId = universalId;
            return this;
        }

        public Builder accountId(BigDecimal accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder clientId(BigDecimal clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder internalClientNumber(String internalClientNumber) {
            this.internalClientNumber = internalClientNumber;
            return this;
        }

        public Builder fileRolePresent(boolean fileRolePresent) {
            this.fileRolePresent = fileRolePresent;
            return this;
        }

        public Builder cardRegistered(boolean cardRegistered) {
            this.cardRegistered = cardRegistered;
            return this;
        }

        public AccountDetails create() {
            return new AccountDetails(this);
        }
    }

}
