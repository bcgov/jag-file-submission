package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigDecimal;

public class EfilingPayment {

    private BigDecimal clientId;
    private BigDecimal paymentAmount;
    private String invoiceNumber;

    public EfilingPayment(BigDecimal clientId, BigDecimal paymentAmount, String invoiceNumber) {

        this.clientId = clientId;
        this.paymentAmount = paymentAmount;
        this.invoiceNumber = invoiceNumber;

    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
}
