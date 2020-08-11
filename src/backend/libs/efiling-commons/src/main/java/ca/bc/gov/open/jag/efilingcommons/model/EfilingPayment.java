package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigDecimal;

public class EfilingPayment {
    private BigDecimal clientId;
    private BigDecimal paymentAmount;

    public BigDecimal getClientId() {
        return clientId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public EfilingPayment(BigDecimal clientId, BigDecimal paymentAmount) {
        this.clientId = clientId;
        this.paymentAmount = paymentAmount;
    }
}
