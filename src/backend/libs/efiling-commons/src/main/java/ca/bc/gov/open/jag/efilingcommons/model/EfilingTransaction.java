package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingTransaction {
    private String approvalCd;
    private String bcOnlineAccountNo;
    private String bcOnlineFeeCodeTxt;
    private String bcOnlineResponseTypeTxt;
    private String bcOnlineReturnCd;
    private String bcOnlineReturnMessageTxt;
    private String bcOnlineSequenceTxt;
    private BigDecimal bcOnlineServiceFeeAmt;
    private String bcOnlineTransactionTypeCd;
    private String creditCardTypeCd;
    private BigDecimal ecommerceTransactionId;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar entDtm;
    private String entUserId;
    private String internalClientNo;
    private String invoiceNo;
    private BigDecimal isoResponseCd;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar processDt;
    private BigDecimal receiptReferenceNo;
    private String referenceMessageTxt;
    private String responseCd;
    private BigDecimal serviceId;
    private String sessionKeyNo;
    private String terminalIdentifierNo;
    private BigDecimal transactionAmt;
    private String transactionStateCd;
    private String transactionSubtypeCd;
    private String transactionTypeCd;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar transactonDtm;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar updDtm;
    private String updUserId;


    public String getApprovalCd() {
        return approvalCd;
    }

    public void setApprovalCd(String approvalCd) {
        this.approvalCd = approvalCd;
    }

    public String getBcOnlineAccountNo() {
        return bcOnlineAccountNo;
    }

    public void setBcOnlineAccountNo(String bcOnlineAccountNo) {
        this.bcOnlineAccountNo = bcOnlineAccountNo;
    }

    public String getBcOnlineFeeCodeTxt() {
        return bcOnlineFeeCodeTxt;
    }

    public void setBcOnlineFeeCodeTxt(String bcOnlineFeeCodeTxt) {
        this.bcOnlineFeeCodeTxt = bcOnlineFeeCodeTxt;
    }

    public String getBcOnlineResponseTypeTxt() {
        return bcOnlineResponseTypeTxt;
    }

    public void setBcOnlineResponseTypeTxt(String bcOnlineResponseTypeTxt) {
        this.bcOnlineResponseTypeTxt = bcOnlineResponseTypeTxt;
    }

    public String getBcOnlineReturnCd() {
        return bcOnlineReturnCd;
    }

    public void setBcOnlineReturnCd(String bcOnlineReturnCd) {
        this.bcOnlineReturnCd = bcOnlineReturnCd;
    }

    public String getBcOnlineReturnMessageTxt() {
        return bcOnlineReturnMessageTxt;
    }

    public void setBcOnlineReturnMessageTxt(String bcOnlineReturnMessageTxt) {
        this.bcOnlineReturnMessageTxt = bcOnlineReturnMessageTxt;
    }

    public String getBcOnlineSequenceTxt() {
        return bcOnlineSequenceTxt;
    }

    public void setBcOnlineSequenceTxt(String bcOnlineSequenceTxt) {
        this.bcOnlineSequenceTxt = bcOnlineSequenceTxt;
    }

    public BigDecimal getBcOnlineServiceFeeAmt() {
        return bcOnlineServiceFeeAmt;
    }

    public void setBcOnlineServiceFeeAmt(BigDecimal bcOnlineServiceFeeAmt) {
        this.bcOnlineServiceFeeAmt = bcOnlineServiceFeeAmt;
    }

    public String getBcOnlineTransactionTypeCd() {
        return bcOnlineTransactionTypeCd;
    }

    public void setBcOnlineTransactionTypeCd(String bcOnlineTransactionTypeCd) {
        this.bcOnlineTransactionTypeCd = bcOnlineTransactionTypeCd;
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

    public void setEcommerceTransactionId(BigDecimal ecommerceTransactionId) {
        this.ecommerceTransactionId = ecommerceTransactionId;
    }

    public XMLGregorianCalendar getEntDtm() {
        return entDtm;
    }

    public void setEntDtm(XMLGregorianCalendar entDtm) {
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

    public BigDecimal getIsoResponseCd() {
        return isoResponseCd;
    }

    public void setIsoResponseCd(BigDecimal isoResponseCd) {
        this.isoResponseCd = isoResponseCd;
    }

    public XMLGregorianCalendar getProcessDt() {
        return processDt;
    }

    public void setProcessDt(XMLGregorianCalendar processDt) {
        this.processDt = processDt;
    }

    public BigDecimal getReceiptReferenceNo() {
        return receiptReferenceNo;
    }

    public void setReceiptReferenceNo(BigDecimal receiptReferenceNo) {
        this.receiptReferenceNo = receiptReferenceNo;
    }

    public String getReferenceMessageTxt() {
        return referenceMessageTxt;
    }

    public void setReferenceMessageTxt(String referenceMessageTxt) {
        this.referenceMessageTxt = referenceMessageTxt;
    }

    public String getResponseCd() {
        return responseCd;
    }

    public void setResponseCd(String responseCd) {
        this.responseCd = responseCd;
    }

    public BigDecimal getServiceId() {
        return serviceId;
    }

    public void setServiceId(BigDecimal serviceId) {
        this.serviceId = serviceId;
    }

    public String getSessionKeyNo() {
        return sessionKeyNo;
    }

    public void setSessionKeyNo(String sessionKeyNo) {
        this.sessionKeyNo = sessionKeyNo;
    }

    public String getTerminalIdentifierNo() {
        return terminalIdentifierNo;
    }

    public void setTerminalIdentifierNo(String terminalIdentifierNo) {
        this.terminalIdentifierNo = terminalIdentifierNo;
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

    public String getTransactionSubtypeCd() {
        return transactionSubtypeCd;
    }

    public void setTransactionSubtypeCd(String transactionSubtypeCd) {
        this.transactionSubtypeCd = transactionSubtypeCd;
    }

    public String getTransactionTypeCd() {
        return transactionTypeCd;
    }

    public void setTransactionTypeCd(String transactionTypeCd) {
        this.transactionTypeCd = transactionTypeCd;
    }

    public XMLGregorianCalendar getTransactonDtm() {
        return transactonDtm;
    }

    public void setTransactonDtm(XMLGregorianCalendar transactonDtm) {
        this.transactonDtm = transactonDtm;
    }

    public XMLGregorianCalendar getUpdDtm() {
        return updDtm;
    }

    public void setUpdDtm(XMLGregorianCalendar updDtm) {
        this.updDtm = updDtm;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public EfilingTransaction(String approvalCd,
                              String bcOnlineAccountNo,
                              String bcOnlineFeeCodeTxt,
                              String bcOnlineResponseTypeTxt,
                              String bcOnlineReturnCd,
                              String bcOnlineReturnMessageTxt,
                              String bcOnlineSequenceTxt,
                              BigDecimal bcOnlineServiceFeeAmt,
                              String bcOnlineTransactionTypeCd,
                              String creditCardTypeCd,
                              BigDecimal ecommerceTransactionId,
                              XMLGregorianCalendar entDtm,
                              String entUserId,
                              String internalClientNo,
                              String invoiceNo,
                              BigDecimal isoResponseCd,
                              XMLGregorianCalendar processDt,
                              BigDecimal receiptReferenceNo,
                              String referenceMessageTxt,
                              String responseCd,
                              BigDecimal serviceId,
                              String sessionKeyNo,
                              String terminalIdentifierNo,
                              BigDecimal transactionAmt,
                              String transactionStateCd,
                              String transactionSubtypeCd,
                              String transactionTypeCd,
                              XMLGregorianCalendar transactonDtm,
                              XMLGregorianCalendar updDtm,
                              String updUserId) {
        this.approvalCd = approvalCd;
        this.bcOnlineAccountNo = bcOnlineAccountNo;
        this.bcOnlineFeeCodeTxt = bcOnlineFeeCodeTxt;
        this.bcOnlineResponseTypeTxt = bcOnlineResponseTypeTxt;
        this.bcOnlineReturnCd = bcOnlineReturnCd;
        this.bcOnlineReturnMessageTxt = bcOnlineReturnMessageTxt;
        this.bcOnlineSequenceTxt = bcOnlineSequenceTxt;
        this.bcOnlineServiceFeeAmt = bcOnlineServiceFeeAmt;
        this.bcOnlineTransactionTypeCd = bcOnlineTransactionTypeCd;
        this.creditCardTypeCd = creditCardTypeCd;
        this.ecommerceTransactionId = ecommerceTransactionId;
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.internalClientNo = internalClientNo;
        this.invoiceNo = invoiceNo;
        this.isoResponseCd = isoResponseCd;
        this.processDt = processDt;
        this.receiptReferenceNo = receiptReferenceNo;
        this.referenceMessageTxt = referenceMessageTxt;
        this.responseCd = responseCd;
        this.serviceId = serviceId;
        this.sessionKeyNo = sessionKeyNo;
        this.terminalIdentifierNo = terminalIdentifierNo;
        this.transactionAmt = transactionAmt;
        this.transactionStateCd = transactionStateCd;
        this.transactionSubtypeCd = transactionSubtypeCd;
        this.transactionTypeCd = transactionTypeCd;
        this.transactonDtm = transactonDtm;
        this.updDtm = updDtm;
        this.updUserId = updUserId;
    }

    public EfilingTransaction() {

    }
}
