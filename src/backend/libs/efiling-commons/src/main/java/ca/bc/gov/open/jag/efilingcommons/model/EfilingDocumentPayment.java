package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingDocumentPayment {
    private XMLGregorianCalendar entDtm;
    private String entUserId;
    private BigDecimal paymentSeqNo;
    private String paymentStatusCd;
    private BigDecimal statutoryFeeAmt;

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

    public BigDecimal getPaymentSeqNo() {
        return paymentSeqNo;
    }

    public void setPaymentSeqNo(BigDecimal paymentSeqNo) {
        this.paymentSeqNo = paymentSeqNo;
    }

    public String getPaymentStatusCd() {
        return paymentStatusCd;
    }

    public void setPaymentStatusCd(String paymentStatusCd) {
        this.paymentStatusCd = paymentStatusCd;
    }

    public BigDecimal getStatutoryFeeAmt() {
        return statutoryFeeAmt;
    }

    public void setStatutoryFeeAmt(BigDecimal statutoryFeeAmt) {
        this.statutoryFeeAmt = statutoryFeeAmt;
    }

    public EfilingDocumentPayment(XMLGregorianCalendar entDtm, String entUserId, BigDecimal paymentSeqNo, String paymentStatusCd, BigDecimal statutoryFeeAmt) {
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.paymentSeqNo = paymentSeqNo;
        this.paymentStatusCd = paymentStatusCd;
        this.statutoryFeeAmt = statutoryFeeAmt;
    }

    public EfilingDocumentPayment() {

    }
}
