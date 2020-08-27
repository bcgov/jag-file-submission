package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingParties {
    protected EfilingIdentificationDetails current;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar entDtm;
    protected String entUserId;
    protected BigDecimal packageId;
    protected BigDecimal partyId;
    protected String partyTypeCd;
    protected String roleTypeCd;
    protected String roleTypeDesc;

    public EfilingIdentificationDetails getCurrent() {
        return current;
    }

    public void setCurrent(EfilingIdentificationDetails current) {
        this.current = current;
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

    public BigDecimal getPartyId() {
        return partyId;
    }

    public void setPartyId(BigDecimal partyId) {
        this.partyId = partyId;
    }

    public String getPartyTypeCd() {
        return partyTypeCd;
    }

    public void setPartyTypeCd(String partyTypeCd) {
        this.partyTypeCd = partyTypeCd;
    }

    public String getRoleTypeCd() {
        return roleTypeCd;
    }

    public void setRoleTypeCd(String roleTypeCd) {
        this.roleTypeCd = roleTypeCd;
    }

    public String getRoleTypeDesc() {
        return roleTypeDesc;
    }

    public void setRoleTypeDesc(String roleTypeDesc) {
        this.roleTypeDesc = roleTypeDesc;
    }

    public EfilingParties(EfilingIdentificationDetails current, XMLGregorianCalendar entDtm, String entUserId, BigDecimal packageId, BigDecimal partyId, String partyTypeCd, String roleTypeCd, String roleTypeDesc) {
        this.current = current;
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.packageId = packageId;
        this.partyId = partyId;
        this.partyTypeCd = partyTypeCd;
        this.roleTypeCd = roleTypeCd;
        this.roleTypeDesc = roleTypeDesc;
    }
    public EfilingParties() {

    }
}
