package ca.bc.gov.open.jag.efilingcommons.submission.models;

import java.math.BigDecimal;

public class FilingPackageRequest {

    private BigDecimal clientId;
    private BigDecimal packageNo;

    public FilingPackageRequest(BigDecimal clientId, BigDecimal packageNo) {
        this.clientId = clientId;
        this.packageNo = packageNo;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public BigDecimal getPackageNo() {
        return packageNo;
    }

}
