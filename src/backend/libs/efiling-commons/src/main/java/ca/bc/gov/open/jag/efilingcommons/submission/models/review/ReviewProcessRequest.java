package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class ReviewProcessRequest {
    private DateTime clientNotificationSentDtm;
    private String contactEmailTxt;
    private String contactFirstGivenNm;
    private String contactOrganizationNm;
    private String contactPhoneNo;
    private String contactSurnameNm;
    private String countryCallingNo;
    private BigDecimal ctryId;
    private DateTime entDtm;
    private String entUserId;
    private DateTime processNotificationSentDtm;
    private BigDecimal processRequestId;
    private String processTypeCd;
    private String processingCommentTxt;
    private DateTime requestDt;
    private DateTime updDtm;
    private String updUserId;

    public ReviewProcessRequest() {}

    public DateTime getClientNotificationSentDtm() {
        return clientNotificationSentDtm;
    }

    public void setClientNotificationSentDtm(DateTime clientNotificationSentDtm) {
        this.clientNotificationSentDtm = clientNotificationSentDtm;
    }

    public String getContactEmailTxt() {
        return contactEmailTxt;
    }

    public void setContactEmailTxt(String contactEmailTxt) {
        this.contactEmailTxt = contactEmailTxt;
    }

    public String getContactFirstGivenNm() {
        return contactFirstGivenNm;
    }

    public void setContactFirstGivenNm(String contactFirstGivenNm) {
        this.contactFirstGivenNm = contactFirstGivenNm;
    }

    public String getContactOrganizationNm() {
        return contactOrganizationNm;
    }

    public void setContactOrganizationNm(String contactOrganizationNm) {
        this.contactOrganizationNm = contactOrganizationNm;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getContactSurnameNm() {
        return contactSurnameNm;
    }

    public void setContactSurnameNm(String contactSurnameNm) {
        this.contactSurnameNm = contactSurnameNm;
    }

    public String getCountryCallingNo() {
        return countryCallingNo;
    }

    public void setCountryCallingNo(String countryCallingNo) {
        this.countryCallingNo = countryCallingNo;
    }

    public BigDecimal getCtryId() {
        return ctryId;
    }

    public void setCtryId(BigDecimal ctryId) {
        this.ctryId = ctryId;
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

    public DateTime getProcessNotificationSentDtm() {
        return processNotificationSentDtm;
    }

    public void setProcessNotificationSentDtm(DateTime processNotificationSentDtm) {
        this.processNotificationSentDtm = processNotificationSentDtm;
    }

    public BigDecimal getProcessRequestId() {
        return processRequestId;
    }

    public void setProcessRequestId(BigDecimal processRequestId) {
        this.processRequestId = processRequestId;
    }

    public String getProcessTypeCd() {
        return processTypeCd;
    }

    public void setProcessTypeCd(String processTypeCd) {
        this.processTypeCd = processTypeCd;
    }

    public String getProcessingCommentTxt() {
        return processingCommentTxt;
    }

    public void setProcessingCommentTxt(String processingCommentTxt) {
        this.processingCommentTxt = processingCommentTxt;
    }

    public DateTime getRequestDt() {
        return requestDt;
    }

    public void setRequestDt(DateTime requestDt) {
        this.requestDt = requestDt;
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
