package ca.bc.gov.open.jag.efilingcommons.submission.models.review.party;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Authorizing {
    private BigDecimal accessRequestId;
    private BigDecimal authorizingPartySeqNo;
    private BigDecimal ceisPartyId;
    private BigDecimal ceisPhysicalFileId;
    private String ceisRoleTypeCd;
    private String displayName;
    private DateTime entDtm;
    private String entUserId;
    private BigDecimal partyId;
    private String roleDesc;
    private DateTime updDtm;
    private String updUserId;

    public Authorizing() {

    }

    public BigDecimal getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(BigDecimal accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public BigDecimal getAuthorizingPartySeqNo() {
        return authorizingPartySeqNo;
    }

    public void setAuthorizingPartySeqNo(BigDecimal authorizingPartySeqNo) {
        this.authorizingPartySeqNo = authorizingPartySeqNo;
    }

    public BigDecimal getCeisPartyId() {
        return ceisPartyId;
    }

    public void setCeisPartyId(BigDecimal ceisPartyId) {
        this.ceisPartyId = ceisPartyId;
    }

    public BigDecimal getCeisPhysicalFileId() {
        return ceisPhysicalFileId;
    }

    public void setCeisPhysicalFileId(BigDecimal ceisPhysicalFileId) {
        this.ceisPhysicalFileId = ceisPhysicalFileId;
    }

    public String getCeisRoleTypeCd() {
        return ceisRoleTypeCd;
    }

    public void setCeisRoleTypeCd(String ceisRoleTypeCd) {
        this.ceisRoleTypeCd = ceisRoleTypeCd;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public DateTime getEntDtm() {
        return entDtm;
    }

    public void setEntDtm(DateTime entDtm) {
        this.entDtm = entDtm;
    }

    public String getEntUserId() {
        return entUserId;
    }

    public void setEntUserId(String entUserId) {
        this.entUserId = entUserId;
    }

    public BigDecimal getPartyId() {
        return partyId;
    }

    public void setPartyId(BigDecimal partyId) {
        this.partyId = partyId;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public DateTime getUpdDtm() {
        return updDtm;
    }

    public void setUpdDtm(DateTime updDtm) {
        this.updDtm = updDtm;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }
}
