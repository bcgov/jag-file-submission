package ca.bc.gov.open.jag.efilingcommons.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class PaymentTransaction {

    private String approvalCd;
    private String creditCardTypeCd;
    private BigDecimal ecommerceTransactionId;
    private DateTime entDtm;
    private String entUserId;
    private String internalClientNo;
    private String invoiceNo;
    private DateTime processDt;
    private String referenceMessageTxt;
    private BigDecimal serviceId;
    private BigDecimal transactionAmt;
    private String transactionStateCd;
    private DateTime transactonDtm;

    public PaymentTransaction(String approvalCd,
                              String creditCardTypeCd,
                              BigDecimal ecommerceTransactionId,
                              DateTime entDtm,
                              String entUserId,
                              String internalClientNo,
                              String invoiceNo,
                              DateTime processDt,
                              String referenceMessageTxt,
                              BigDecimal serviceId,
                              BigDecimal transactionAmt,
                              String transactionStateCd,
                              DateTime transactonDtm) {
        this.approvalCd = approvalCd;
        this.creditCardTypeCd = creditCardTypeCd;
        this.ecommerceTransactionId = ecommerceTransactionId;
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.internalClientNo = internalClientNo;
        this.invoiceNo = invoiceNo;
        this.processDt = processDt;
        this.referenceMessageTxt = referenceMessageTxt;
        this.serviceId = serviceId;
        this.transactionAmt = transactionAmt;
        this.transactionStateCd = transactionStateCd;
        this.transactonDtm = transactonDtm;
    }

    public PaymentTransaction() {

    }


    public String getApprovalCd() {
        return approvalCd;
    }

    public void setApprovalCd(String approvalCd) {
        this.approvalCd = approvalCd;
    }

    public String getCreditCardTypeCd() {
        return creditCardTypeCd;
    }

    public void setCreditCardTypeCd(String creditCardTypeCd) {
        this.creditCardTypeCd = creditCardTypeCd;
    }

    public BigDecimal getEcommerceTransactionId() {
        return ecommerceTransactionId;
    }

    public void setEcommerceTransactionId(BigDecimal ecommerceTransactionId) { this.ecommerceTransactionId = ecommerceTransactionId; }

    public DateTime getEntDtm() { return entDtm; }

    public void setEntDtm(DateTime entDtm) {
        this.entDtm = entDtm;
    }

    public String getEntUserId() {
        return entUserId;
    }

    public void setEntUserId(String entUserId) {
        this.entUserId = entUserId;
    }

    public String getInternalClientNo() {
        return internalClientNo;
    }

    public void setInternalClientNo(String internalClientNo) {
        this.internalClientNo = internalClientNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public DateTime getProcessDt() {
        return processDt;
    }

    public void setProcessDt(DateTime processDt) {
        this.processDt = processDt;
    }

    public String getReferenceMessageTxt() {
        return referenceMessageTxt;
    }

    public void setReferenceMessageTxt(String referenceMessageTxt) {
        this.referenceMessageTxt = referenceMessageTxt;
    }

    public BigDecimal getServiceId() {
        return serviceId;
    }

    public void setServiceId(BigDecimal serviceId) {
        this.serviceId = serviceId;
    }

    public BigDecimal getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(BigDecimal transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getTransactionStateCd() {
        return transactionStateCd;
    }

    public void setTransactionStateCd(String transactionStateCd) {
        this.transactionStateCd = transactionStateCd;
    }

    public DateTime getTransactonDtm() {
        return transactonDtm;
    }

    public void setTransactonDtm(DateTime transactonDtm) {
        this.transactonDtm = transactonDtm;
    }

}
