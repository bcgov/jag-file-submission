package ca.bc.gov.open.jag.efilingcommons.submission.models;

import java.math.BigDecimal;

public class FilingPackageRequest {

    private BigDecimal clientId;
    private BigDecimal accountId;
    private BigDecimal packageNo;
    private String parentApplication;

    public FilingPackageRequest(BigDecimal clientId, BigDecimal accountId, BigDecimal packageNo, String parentApplication) {
        this.clientId = clientId;
        this.accountId = accountId;
        this.packageNo = packageNo;
        this.parentApplication = parentApplication;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public BigDecimal getPackageNo() {
        return packageNo;
    }

    public String getParentApplication() { return parentApplication; }

    public BigDecimal getAccountId() {
        return accountId;
    }

}
