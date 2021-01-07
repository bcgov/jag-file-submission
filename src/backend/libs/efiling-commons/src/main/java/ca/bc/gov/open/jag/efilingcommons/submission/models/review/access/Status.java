package ca.bc.gov.open.jag.efilingcommons.submission.models.review.access;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Status {
    private BigDecimal accessRequestId;
    private BigDecimal accessStatusSeqNo;
    private String accessStatusTypeCd;
    private BigDecimal accountId;
    private BigDecimal agenId;
    private BigDecimal clientId;
    private String commentTxt;
    private DateTime entDtm;
    private String entUserId;
    private BigDecimal paasSeqNo;
    private BigDecimal partId;
    private DateTime statusDtm;
    private DateTime updDtm;
    private String updUserId;

    public Status() {

    }

    public BigDecimal getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(BigDecimal accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public BigDecimal getAccessStatusSeqNo() {
        return accessStatusSeqNo;
    }

    public void setAccessStatusSeqNo(BigDecimal accessStatusSeqNo) {
        this.accessStatusSeqNo = accessStatusSeqNo;
    }

    public String getAccessStatusTypeCd() {
        return accessStatusTypeCd;
    }

    public void setAccessStatusTypeCd(String accessStatusTypeCd) {
        this.accessStatusTypeCd = accessStatusTypeCd;
    }

    public BigDecimal getAccountId() {
        return accountId;
    }

    public void setAccountId(BigDecimal accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAgenId() {
        return agenId;
    }

    public void setAgenId(BigDecimal agenId) {
        this.agenId = agenId;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public void setClientId(BigDecimal clientId) {
        this.clientId = clientId;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
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

    public BigDecimal getPaasSeqNo() {
        return paasSeqNo;
    }

    public void setPaasSeqNo(BigDecimal paasSeqNo) {
        this.paasSeqNo = paasSeqNo;
    }

    public BigDecimal getPartId() {
        return partId;
    }

    public void setPartId(BigDecimal partId) {
        this.partId = partId;
    }

    public DateTime getStatusDtm() {
        return statusDtm;
    }

    public void setStatusDtm(DateTime statusDtm) {
        this.statusDtm = statusDtm;
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
