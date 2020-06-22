package ca.bc.gov.open.jag.efilingaccountclient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CsoAccountDetails {


    public CsoAccountDetails(
            @JsonProperty("accountId") BigDecimal accountId,
            @JsonProperty("clientId") BigDecimal clientId) {

        this.accountId = accountId;
        this.clientId = clientId;
    }

    @JsonCreator
    public CsoAccountDetails(
            @JsonProperty("accountId") BigDecimal accountId,
            @JsonProperty("clientId") BigDecimal clientId,
            @JsonProperty("roles") List<String> roles) {

        this(accountId, clientId);

        for (String role: roles) {
            String toAdd = role.toLowerCase();
            this.roles.add(toAdd);
        }
    }

    private BigDecimal accountId;
    private BigDecimal clientId;
    private List<String> roles = new ArrayList<>();

    public BigDecimal getAccountId() {
        return accountId;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public List<String> getRoles() { return roles; }

    public void addRole(String role) {
        this.roles.add(role.toLowerCase());
    }

    public boolean HasRole(String role) {
        String toFind = role.toLowerCase();
        return roles.stream().anyMatch(r -> r.equals(toFind));
    }

}
