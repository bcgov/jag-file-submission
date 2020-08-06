package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingPackageAuthority {
    private BigDecimal accountId;
    private BigDecimal clientId;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar entDtm;
    private String entUserId;
    private BigDecimal packageId;
    private String privilegeCd;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar updDtm;
    private String updUserId;

    public BigDecimal getAccountId() {
        return accountId;
    }

    public void setAccountId(BigDecimal accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public void setClientId(BigDecimal clientId) {
        this.clientId = clientId;
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

    public BigDecimal getPackageId() {
        return packageId;
    }

    public void setPackageId(BigDecimal packageId) {
        this.packageId = packageId;
    }

    public String getPrivilegeCd() {
        return privilegeCd;
    }

    public void setPrivilegeCd(String privilegeCd) {
        this.privilegeCd = privilegeCd;
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

    public EfilingPackageAuthority(BigDecimal accountId, BigDecimal clientId, XMLGregorianCalendar entDtm, String entUserId, BigDecimal packageId, String privilegeCd, XMLGregorianCalendar updDtm, String updUserId) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.packageId = packageId;
        this.privilegeCd = privilegeCd;
        this.updDtm = updDtm;
        this.updUserId = updUserId;
    }

    public EfilingPackageAuthority() {

    }
}

