package ca.bc.gov.open.jag.efilingcommons.submission.models.review.access;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Period {
    private BigDecimal accessPeriodSeqNo;
    private BigDecimal accessRequestId;
    private DateTime corAcknowledgeDt;
    private DateTime endDt;
    private DateTime entDtm;
    private String entUserId;
    private Boolean expiryNotificationSentYn;
    private DateTime startDt;
    private DateTime updDtm;
    private String updUserId;

    public Period() {

    }

    public BigDecimal getAccessPeriodSeqNo() {
        return accessPeriodSeqNo;
    }

    public void setAccessPeriodSeqNo(BigDecimal accessPeriodSeqNo) {
        this.accessPeriodSeqNo = accessPeriodSeqNo;
    }

    public BigDecimal getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(BigDecimal accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public DateTime getCorAcknowledgeDt() {
        return corAcknowledgeDt;
    }

    public void setCorAcknowledgeDt(DateTime corAcknowledgeDt) {
        this.corAcknowledgeDt = corAcknowledgeDt;
    }

    public DateTime getEndDt() {
        return endDt;
    }

    public void setEndDt(DateTime endDt) {
        this.endDt = endDt;
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

    public Boolean getExpiryNotificationSentYn() {
        return expiryNotificationSentYn;
    }

    public void setExpiryNotificationSentYn(Boolean expiryNotificationSentYn) {
        this.expiryNotificationSentYn = expiryNotificationSentYn;
    }

    public DateTime getStartDt() {
        return startDt;
    }

    public void setStartDt(DateTime startDt) {
        this.startDt = startDt;
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
