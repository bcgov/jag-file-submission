package ca.bc.gov.open.jag.efilingaccountclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsoAccountDetails {

    CsoAccountDetails(String accountId, String clientId, String[] roles) {

        this.accountId = accountId;
        this.clientId = clientId;

        for (String role: roles) {
            String toAdd = role.toLowerCase();
            this.roles.add(toAdd);
        }
    }

    private String accountId;
    private String clientId;
    private List<String> roles;

    public String getAccountId() {
        return accountId;
    }

    public String getClientId() {
        return clientId;
    }

    public boolean HasRole(String role) {
        String toFind = role.toLowerCase();
        return roles.indexOf(toFind) > 0;
    }
}
