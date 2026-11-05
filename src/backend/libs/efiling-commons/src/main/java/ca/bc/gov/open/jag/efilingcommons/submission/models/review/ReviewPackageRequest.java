package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import ca.bc.gov.open.jag.efilingcommons.submission.models.review.access.Period;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.access.Status;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.party.Authorizing;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public class ReviewPackageRequest {
    private String accessEntitlementCd;
    private DateTime accessRequestDt;
    private BigDecimal accessRequestId;
    private String accessStatusTypeDesc;
    private String accrTypeCd;
    private BigDecimal ceisPhysicalFileId;
    private BigDecimal createdForAccountId;
    private BigDecimal createdForClientId;
    private DateTime entDtm;
    private String entUserId;
    private BigDecimal justinPhysId;
    private BigDecimal packageId;
    private BigDecimal requestedByAccountId;
    private BigDecimal requestedByClientId;
    private DateTime updDtm;
    private String updUserId;
    private Status accessStatus;
    private Period accessPeriod;
    private List<Authorizing> authorizingParties;

    public ReviewPackageRequest() {

    }

    public String getAccessEntitlementCd() {
        return accessEntitlementCd;
    }

    public void setAccessEntitlementCd(String accessEntitlementCd) {
        this.accessEntitlementCd = accessEntitlementCd;
    }

    public DateTime getAccessRequestDt() {
        return accessRequestDt;
    }

    public void setAccessRequestDt(DateTime accessRequestDt) {
        this.accessRequestDt = accessRequestDt;
    }

    public BigDecimal getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(BigDecimal accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public String getAccessStatusTypeDesc() {
        return accessStatusTypeDesc;
    }

    public void setAccessStatusTypeDesc(String accessStatusTypeDesc) {
        this.accessStatusTypeDesc = accessStatusTypeDesc;
    }

    public String getAccrTypeCd() {
        return accrTypeCd;
    }

    public void setAccrTypeCd(String accrTypeCd) {
        this.accrTypeCd = accrTypeCd;
    }

    public BigDecimal getCeisPhysicalFileId() {
        return ceisPhysicalFileId;
    }

    public void setCeisPhysicalFileId(BigDecimal ceisPhysicalFileId) {
        this.ceisPhysicalFileId = ceisPhysicalFileId;
    }

    public BigDecimal getCreatedForAccountId() {
        return createdForAccountId;
    }

    public void setCreatedForAccountId(BigDecimal createdForAccountId) {
        this.createdForAccountId = createdForAccountId;
    }

    public BigDecimal getCreatedForClientId() {
        return createdForClientId;
    }

    public void setCreatedForClientId(BigDecimal createdForClientId) {
        this.createdForClientId = createdForClientId;
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

    public BigDecimal getJustinPhysId() {
        return justinPhysId;
    }

    public void setJustinPhysId(BigDecimal justinPhysId) {
        this.justinPhysId = justinPhysId;
    }

    public BigDecimal getPackageId() {
        return packageId;
    }

    public void setPackageId(BigDecimal packageId) {
        this.packageId = packageId;
    }

    public BigDecimal getRequestedByAccountId() {
        return requestedByAccountId;
    }

    public void setRequestedByAccountId(BigDecimal requestedByAccountId) {
        this.requestedByAccountId = requestedByAccountId;
    }

    public BigDecimal getRequestedByClientId() {
        return requestedByClientId;
    }

    public void setRequestedByClientId(BigDecimal requestedByClientId) {
        this.requestedByClientId = requestedByClientId;
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

    public Status getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(Status accessStatus) {
        this.accessStatus = accessStatus;
    }

    public Period getAccessPeriod() {
        return accessPeriod;
    }

    public void setAccessPeriod(Period accessPeriod) {
        this.accessPeriod = accessPeriod;
    }

    public List<Authorizing> getAuthorizingParties() {
        return authorizingParties;
    }

    public void setAuthorizingParties(List<Authorizing> authorizingParties) {
        this.authorizingParties = authorizingParties;
    }
}
