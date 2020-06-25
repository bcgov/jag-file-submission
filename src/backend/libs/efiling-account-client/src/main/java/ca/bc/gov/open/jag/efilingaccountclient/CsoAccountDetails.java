package ca.bc.gov.open.jag.efilingaccountclient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CsoAccountDetails {

    @JsonCreator
    public CsoAccountDetails(
            @JsonProperty("accountId") BigDecimal accountId,
            @JsonProperty("clientId") BigDecimal clientId,
            @JsonProperty("hasEfileRole") boolean hasEfileRole) {

        this.accountId = accountId;
        this.clientId = clientId;
        this.hasEfileRole = hasEfileRole;
    }

    private BigDecimal accountId;
    private BigDecimal clientId;
    private boolean hasEfileRole;

    public BigDecimal getAccountId() { return accountId; }
    public BigDecimal getClientId() {
        return clientId;
    }
    public boolean getHasEfileRole() { return hasEfileRole; }
}
