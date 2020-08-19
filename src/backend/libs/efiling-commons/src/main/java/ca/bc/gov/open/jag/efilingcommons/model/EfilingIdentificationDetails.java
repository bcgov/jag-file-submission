package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingIdentificationDetails {
    protected XMLGregorianCalendar entDtm;
    protected String entUserId;
    protected String firstGivenNm;
    protected BigDecimal identificationDetailSeqNo;
    protected String nameTypeCd;
    protected String organizationNm;
    protected BigDecimal partyId;
    protected String secondGivenNm;
    protected String surnameNm;
    protected String thirdGivenNm;

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

    public String getFirstGivenNm() {
        return firstGivenNm;
    }

    public void setFirstGivenNm(String firstGivenNm) {
        this.firstGivenNm = firstGivenNm;
    }

    public BigDecimal getIdentificationDetailSeqNo() {
        return identificationDetailSeqNo;
    }

    public void setIdentificationDetailSeqNo(BigDecimal identificationDetailSeqNo) {
        this.identificationDetailSeqNo = identificationDetailSeqNo;
    }

    public String getNameTypeCd() {
        return nameTypeCd;
    }

    public void setNameTypeCd(String nameTypeCd) {
        this.nameTypeCd = nameTypeCd;
    }

    public String getOrganizationNm() {
        return organizationNm;
    }

    public void setOrganizationNm(String organizationNm) {
        this.organizationNm = organizationNm;
    }

    public BigDecimal getPartyId() {
        return partyId;
    }

    public void setPartyId(BigDecimal partyId) {
        this.partyId = partyId;
    }

    public String getSecondGivenNm() {
        return secondGivenNm;
    }

    public void setSecondGivenNm(String secondGivenNm) {
        this.secondGivenNm = secondGivenNm;
    }

    public String getSurnameNm() {
        return surnameNm;
    }

    public void setSurnameNm(String surnameNm) {
        this.surnameNm = surnameNm;
    }

    public String getThirdGivenNm() {
        return thirdGivenNm;
    }

    public void setThirdGivenNm(String thirdGivenNm) {
        this.thirdGivenNm = thirdGivenNm;
    }

    public EfilingIdentificationDetails(XMLGregorianCalendar entDtm, String entUserId, String firstGivenNm, BigDecimal identificationDetailSeqNo, String nameTypeCd, String organizationNm, BigDecimal partyId, String secondGivenNm, String surnameNm, String thirdGivenNm) {
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.firstGivenNm = firstGivenNm;
        this.identificationDetailSeqNo = identificationDetailSeqNo;
        this.nameTypeCd = nameTypeCd;
        this.organizationNm = organizationNm;
        this.partyId = partyId;
        this.secondGivenNm = secondGivenNm;
        this.surnameNm = surnameNm;
        this.thirdGivenNm = thirdGivenNm;
    }

    public EfilingIdentificationDetails() {

    }
}
