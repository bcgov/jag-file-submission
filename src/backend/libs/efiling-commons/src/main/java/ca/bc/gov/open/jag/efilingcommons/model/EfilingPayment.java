package ca.bc.gov.open.jag.efilingcommons.model;

import java.math.BigDecimal;

public class EfilingPayment {

    private BigDecimal serviceId;
    private BigDecimal paymentAmount;
    private String invoiceNumber;
    private String internalClientNumber;

    public EfilingPayment(BigDecimal serviceId, BigDecimal paymentAmount, String invoiceNumber, String internalClientNumber) {

        this.serviceId = serviceId;
        this.paymentAmount = paymentAmount;
        this.invoiceNumber = invoiceNumber;
        this.internalClientNumber = internalClientNumber;

    }

    public BigDecimal getServiceId() {
        return serviceId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public String getInvoiceNumber() { return invoiceNumber; }

    public String getInternalClientNumber() { return internalClientNumber; }
}
