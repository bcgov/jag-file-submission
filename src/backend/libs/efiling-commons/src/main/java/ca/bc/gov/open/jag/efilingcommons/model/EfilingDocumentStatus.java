package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingDocumentStatus {
    private BigDecimal documentStatusSeqNo;
    private String documentStatusTypeCd;
    private XMLGregorianCalendar entDtm;
    private String entUserId;
    private XMLGregorianCalendar statusDtm;

    public BigDecimal getDocumentStatusSeqNo() {
        return documentStatusSeqNo;
    }

    public void setDocumentStatusSeqNo(BigDecimal documentStatusSeqNo) {
        this.documentStatusSeqNo = documentStatusSeqNo;
    }

    public String getDocumentStatusTypeCd() {
        return documentStatusTypeCd;
    }

    public void setDocumentStatusTypeCd(String documentStatusTypeCd) {
        this.documentStatusTypeCd = documentStatusTypeCd;
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

    public XMLGregorianCalendar getStatusDtm() {
        return statusDtm;
    }

    public void setStatusDtm(XMLGregorianCalendar statusDtm) {
        this.statusDtm = statusDtm;
    }

    public EfilingDocumentStatus(BigDecimal documentStatusSeqNo, String documentStatusTypeCd, XMLGregorianCalendar entDtm, String entUserId, XMLGregorianCalendar statusDtm) {
        this.documentStatusSeqNo = documentStatusSeqNo;
        this.documentStatusTypeCd = documentStatusTypeCd;
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.statusDtm = statusDtm;
    }

    public EfilingDocumentStatus() {

    }
}
