package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class PackagePayment {
    private Boolean feeExmpt;
    private BigDecimal paymentCategory;
    private BigDecimal processedAmt;
    private BigDecimal serviceId;
    private BigDecimal submittedAmt;
    private String transactionDesc;
    private DateTime transactionDtm;

    public PackagePayment() {

    }

    public Boolean getFeeExmpt() {
        return feeExmpt;
    }

    public void setFeeExmpt(Boolean feeExmpt) {
        this.feeExmpt = feeExmpt;
    }

    public BigDecimal getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(BigDecimal paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public BigDecimal getProcessedAmt() {
        return processedAmt;
    }

    public void setProcessedAmt(BigDecimal processedAmt) {
        this.processedAmt = processedAmt;
    }

    public BigDecimal getServiceId() {
        return serviceId;
    }

    public void setServiceId(BigDecimal serviceId) {
        this.serviceId = serviceId;
    }

    public BigDecimal getSubmittedAmt() {
        return submittedAmt;
    }

    public void setSubmittedAmt(BigDecimal submittedAmt) {
        this.submittedAmt = submittedAmt;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    public DateTime getTransactionDtm() {
        return transactionDtm;
    }

    public void setTransactionDtm(DateTime transactionDtm) {
        this.transactionDtm = transactionDtm;
    }
}
