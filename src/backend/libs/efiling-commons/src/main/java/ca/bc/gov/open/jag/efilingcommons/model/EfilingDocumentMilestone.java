package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingDocumentMilestone {
    private XMLGregorianCalendar entDtm;
    private String entUserId;
    private XMLGregorianCalendar milestoneDtm;
    private BigDecimal milestoneSeqNo;
    private String milestoneTypeCd;

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

    public XMLGregorianCalendar getMilestoneDtm() {
        return milestoneDtm;
    }

    public void setMilestoneDtm(XMLGregorianCalendar milestoneDtm) {
        this.milestoneDtm = milestoneDtm;
    }

    public BigDecimal getMilestoneSeqNo() {
        return milestoneSeqNo;
    }

    public void setMilestoneSeqNo(BigDecimal milestoneSeqNo) {
        this.milestoneSeqNo = milestoneSeqNo;
    }

    public String getMilestoneTypeCd() {
        return milestoneTypeCd;
    }

    public void setMilestoneTypeCd(String milestoneTypeCd) {
        this.milestoneTypeCd = milestoneTypeCd;
    }

    public EfilingDocumentMilestone(XMLGregorianCalendar entDtm, String entUserId, XMLGregorianCalendar milestoneDtm, BigDecimal milestoneSeqNo, String milestoneTypeCd) {
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.milestoneDtm = milestoneDtm;
        this.milestoneSeqNo = milestoneSeqNo;
        this.milestoneTypeCd = milestoneTypeCd;
    }

    public EfilingDocumentMilestone() {

    }
}
