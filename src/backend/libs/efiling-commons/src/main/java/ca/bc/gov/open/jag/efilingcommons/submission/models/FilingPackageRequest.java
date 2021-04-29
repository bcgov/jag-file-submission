package ca.bc.gov.open.jag.efilingcommons.submission.models;

import java.math.BigDecimal;

public class FilingPackageRequest {

    private BigDecimal clientId;
    private BigDecimal packageNo;
    private String parentApplication;

    public FilingPackageRequest(BigDecimal clientId, BigDecimal packageNo, String parentApplication) {
        this.clientId = clientId;
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
}
