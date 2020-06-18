package ca.bc.gov.open.jag.efilingaccountclient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CsoAccountDetails {

    
    public CsoAccountDetails(
            @JsonProperty("accountId") String accountId,
            @JsonProperty("clientId") String clientId) {

        this.accountId = accountId;
        this.clientId = clientId;
    }

    @JsonCreator
    public CsoAccountDetails(
            @JsonProperty("accountId") String accountId,
            @JsonProperty("clientId") String clientId,
            @JsonProperty("roles") List<String> roles) {

        this(accountId, clientId);

        for (String role: roles) {
            String toAdd = role.toLowerCase();
            this.roles.add(toAdd);
        }
    }

    private String accountId;
    private String clientId;
    private List<String> roles = new ArrayList<>();

    public String getAccountId() {
        return accountId;
    }

    public String getClientId() {
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
