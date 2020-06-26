package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AccountDetails {

    private BigDecimal accountId;
    private BigDecimal clientId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean hasEfileRole;


    public AccountDetails() {}

    @JsonCreator
    public AccountDetails(@JsonProperty("accountId") BigDecimal accountId,
                          @JsonProperty("clientId") BigDecimal clientId,
                          @JsonProperty("hasEfileRole") boolean hasEfileRole,
                          @JsonProperty("firstName") String firstName,
                          @JsonProperty("lastName") String lastName,
                          @JsonProperty("email")String email)
    {
        this.accountId = accountId;
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hasEfileRole = hasEfileRole;
    }

    public BigDecimal getAccountId() {
        return accountId;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean hasEfileRole() {
        return hasEfileRole;
    }


    public void setAccountId(BigDecimal accountId) {
        this.accountId = accountId;
    }

    public void setClientId(BigDecimal clientId) {
        this.clientId = clientId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHasEfileRole(boolean hasEfileRole) {
        this.hasEfileRole = hasEfileRole;
    }
}
