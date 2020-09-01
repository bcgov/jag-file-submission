package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.List;

public class EfilingFilingPackage {
    private String applicationCd;
    private String applicationReferenceGuid;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar autoProcessEndDtm;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar autoProcessStartDtm;
    private Boolean automatedProcessYn;
    private Boolean cfcsaYn;
    private BigDecimal checkedOutByAgenId;
    private BigDecimal checkedOutByPaasSeqNo;
    private BigDecimal checkedOutByPartId;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar checkedOutDtm;
    private String clientFileNo;
    private String courtFileNo;
    private Boolean delayProcessing;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar entDtm;
    private String entUserId;
    private Boolean existingCourtFileYn;
    private Boolean feeExemptYn;
    private String ldcxCourtClassCd;
    private String ldcxCourtDivisionCd;
    private String ldcxCourtLevelCd;
    private String notificationEmailTxt;
    private Boolean notificationRequiredYn;
    @XmlElement(nillable = true)
    private List<EfilingPackageAuthority> packageControls;
    private BigDecimal packageId;
    private Boolean processingCompleteYn;
    private BigDecimal resubmissionOfPackageId;
    private String reviewerNotesTxt;
    private BigDecimal serviceId;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar submitDtm;
    private BigDecimal submittedByAccountId;
    private BigDecimal submittedByClientId;
    private BigDecimal submittedToAgenId;
    private String submitterCommentTxt;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar updDtm;
    private String updUserId;

    public String getApplicationCd() {
        return applicationCd;
    }

    public void setApplicationCd(String applicationCd) {
        this.applicationCd = applicationCd;
    }

    public String getApplicationReferenceGuid() {
        return applicationReferenceGuid;
    }

    public void setApplicationReferenceGuid(String applicationReferenceGuid) {
        this.applicationReferenceGuid = applicationReferenceGuid;
    }

    public XMLGregorianCalendar getAutoProcessEndDtm() {
        return autoProcessEndDtm;
    }

    public void setAutoProcessEndDtm(XMLGregorianCalendar autoProcessEndDtm) {
        this.autoProcessEndDtm = autoProcessEndDtm;
    }

    public XMLGregorianCalendar getAutoProcessStartDtm() {
        return autoProcessStartDtm;
    }

    public void setAutoProcessStartDtm(XMLGregorianCalendar autoProcessStartDtm) {
        this.autoProcessStartDtm = autoProcessStartDtm;
    }

    public Boolean getAutomatedProcessYn() {
        return automatedProcessYn;
    }

    public void setAutomatedProcessYn(Boolean automatedProcessYn) {
        this.automatedProcessYn = automatedProcessYn;
    }

    public Boolean getCfcsaYn() {
        return cfcsaYn;
    }

    public void setCfcsaYn(Boolean cfcsaYn) {
        this.cfcsaYn = cfcsaYn;
    }

    public BigDecimal getCheckedOutByAgenId() {
        return checkedOutByAgenId;
    }

    public void setCheckedOutByAgenId(BigDecimal checkedOutByAgenId) {
        this.checkedOutByAgenId = checkedOutByAgenId;
    }

    public BigDecimal getCheckedOutByPaasSeqNo() {
        return checkedOutByPaasSeqNo;
    }

    public void setCheckedOutByPaasSeqNo(BigDecimal checkedOutByPaasSeqNo) {
        this.checkedOutByPaasSeqNo = checkedOutByPaasSeqNo;
    }

    public BigDecimal getCheckedOutByPartId() {
        return checkedOutByPartId;
    }

    public void setCheckedOutByPartId(BigDecimal checkedOutByPartId) {
        this.checkedOutByPartId = checkedOutByPartId;
    }

    public XMLGregorianCalendar getCheckedOutDtm() {
        return checkedOutDtm;
    }

    public void setCheckedOutDtm(XMLGregorianCalendar checkedOutDtm) {
        this.checkedOutDtm = checkedOutDtm;
    }

    public String getClientFileNo() {
        return clientFileNo;
    }

    public void setClientFileNo(String clientFileNo) {
        this.clientFileNo = clientFileNo;
    }

    public String getCourtFileNo() {
        return courtFileNo;
    }

    public void setCourtFileNo(String courtFileNo) {
        this.courtFileNo = courtFileNo;
    }

    public Boolean getDelayProcessing() {
        return delayProcessing;
    }

    public void setDelayProcessing(Boolean delayProcessing) {
        this.delayProcessing = delayProcessing;
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

    public Boolean getExistingCourtFileYn() {
        return existingCourtFileYn;
    }

    public void setExistingCourtFileYn(Boolean existingCourtFileYn) {
        this.existingCourtFileYn = existingCourtFileYn;
    }

    public Boolean getFeeExemptYn() {
        return feeExemptYn;
    }

    public void setFeeExemptYn(Boolean feeExemptYn) {
        this.feeExemptYn = feeExemptYn;
    }

    public String getLdcxCourtClassCd() {
        return ldcxCourtClassCd;
    }

    public void setLdcxCourtClassCd(String ldcxCourtClassCd) {
        this.ldcxCourtClassCd = ldcxCourtClassCd;
    }

    public String getLdcxCourtDivisionCd() {
        return ldcxCourtDivisionCd;
    }

    public void setLdcxCourtDivisionCd(String ldcxCourtDivisionCd) {
        this.ldcxCourtDivisionCd = ldcxCourtDivisionCd;
    }

    public String getLdcxCourtLevelCd() {
        return ldcxCourtLevelCd;
    }

    public void setLdcxCourtLevelCd(String ldcxCourtLevelCd) {
        this.ldcxCourtLevelCd = ldcxCourtLevelCd;
    }

    public String getNotificationEmailTxt() {
        return notificationEmailTxt;
    }

    public void setNotificationEmailTxt(String notificationEmailTxt) {
        this.notificationEmailTxt = notificationEmailTxt;
    }

    public Boolean getNotificationRequiredYn() {
        return notificationRequiredYn;
    }

    public void setNotificationRequiredYn(Boolean notificationRequiredYn) {
        this.notificationRequiredYn = notificationRequiredYn;
    }

    public List<EfilingPackageAuthority> getPackageControls() {
        return packageControls;
    }

    public void setPackageControls(List<EfilingPackageAuthority> packageControls) {
        this.packageControls = packageControls;
    }

    public BigDecimal getPackageId() {
        return packageId;
    }

    public void setPackageId(BigDecimal packageId) {
        this.packageId = packageId;
    }

    public Boolean getProcessingCompleteYn() {
        return processingCompleteYn;
    }

    public void setProcessingCompleteYn(Boolean processingCompleteYn) {
        this.processingCompleteYn = processingCompleteYn;
    }

    public BigDecimal getResubmissionOfPackageId() {
        return resubmissionOfPackageId;
    }

    public void setResubmissionOfPackageId(BigDecimal resubmissionOfPackageId) {
        this.resubmissionOfPackageId = resubmissionOfPackageId;
    }

    public String getReviewerNotesTxt() {
        return reviewerNotesTxt;
    }

    public void setReviewerNotesTxt(String reviewerNotesTxt) {
        this.reviewerNotesTxt = reviewerNotesTxt;
    }

    public BigDecimal getServiceId() {
        return serviceId;
    }

    public void setServiceId(BigDecimal serviceId) {
        this.serviceId = serviceId;
    }

    public XMLGregorianCalendar getSubmitDtm() {
        return submitDtm;
    }

    public void setSubmitDtm(XMLGregorianCalendar submitDtm) {
        this.submitDtm = submitDtm;
    }

    public BigDecimal getSubmittedByAccountId() {
        return submittedByAccountId;
    }

    public void setSubmittedByAccountId(BigDecimal submittedByAccountId) {
        this.submittedByAccountId = submittedByAccountId;
    }

    public BigDecimal getSubmittedByClientId() {
        return submittedByClientId;
    }

    public void setSubmittedByClientId(BigDecimal submittedByClientId) {
        this.submittedByClientId = submittedByClientId;
    }

    public BigDecimal getSubmittedToAgenId() {
        return submittedToAgenId;
    }

    public void setSubmittedToAgenId(BigDecimal submittedToAgenId) {
        this.submittedToAgenId = submittedToAgenId;
    }

    public String getSubmitterCommentTxt() {
        return submitterCommentTxt;
    }

    public void setSubmitterCommentTxt(String submitterCommentTxt) {
        this.submitterCommentTxt = submitterCommentTxt;
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

    public EfilingFilingPackage(String applicationCd, String applicationReferenceGuid, XMLGregorianCalendar autoProcessEndDtm, XMLGregorianCalendar autoProcessStartDtm, Boolean automatedProcessYn, Boolean cfcsaYn, BigDecimal checkedOutByAgenId, BigDecimal checkedOutByPaasSeqNo, BigDecimal checkedOutByPartId, XMLGregorianCalendar checkedOutDtm, String clientFileNo, String courtFileNo, Boolean delayProcessing, XMLGregorianCalendar entDtm, String entUserId, Boolean existingCourtFileYn, Boolean feeExemptYn, String ldcxCourtClassCd, String ldcxCourtDivisionCd, String ldcxCourtLevelCd, String notificationEmailTxt, Boolean notificationRequiredYn, List<EfilingPackageAuthority> packageControls, BigDecimal packageId, Boolean processingCompleteYn, BigDecimal resubmissionOfPackageId, String reviewerNotesTxt, BigDecimal serviceId, XMLGregorianCalendar submitDtm, BigDecimal submittedByAccountId, BigDecimal submittedByClientId, BigDecimal submittedToAgenId, String submitterCommentTxt, XMLGregorianCalendar updDtm, String updUserId) {
        this.applicationCd = applicationCd;
        this.applicationReferenceGuid = applicationReferenceGuid;
        this.autoProcessEndDtm = autoProcessEndDtm;
        this.autoProcessStartDtm = autoProcessStartDtm;
        this.automatedProcessYn = automatedProcessYn;
        this.cfcsaYn = cfcsaYn;
        this.checkedOutByAgenId = checkedOutByAgenId;
        this.checkedOutByPaasSeqNo = checkedOutByPaasSeqNo;
        this.checkedOutByPartId = checkedOutByPartId;
        this.checkedOutDtm = checkedOutDtm;
        this.clientFileNo = clientFileNo;
        this.courtFileNo = courtFileNo;
        this.delayProcessing = delayProcessing;
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.existingCourtFileYn = existingCourtFileYn;
        this.feeExemptYn = feeExemptYn;
        this.ldcxCourtClassCd = ldcxCourtClassCd;
        this.ldcxCourtDivisionCd = ldcxCourtDivisionCd;
        this.ldcxCourtLevelCd = ldcxCourtLevelCd;
        this.notificationEmailTxt = notificationEmailTxt;
        this.notificationRequiredYn = notificationRequiredYn;
        this.packageControls = packageControls;
        this.packageId = packageId;
        this.processingCompleteYn = processingCompleteYn;
        this.resubmissionOfPackageId = resubmissionOfPackageId;
        this.reviewerNotesTxt = reviewerNotesTxt;
        this.serviceId = serviceId;
        this.submitDtm = submitDtm;
        this.submittedByAccountId = submittedByAccountId;
        this.submittedByClientId = submittedByClientId;
        this.submittedToAgenId = submittedToAgenId;
        this.submitterCommentTxt = submitterCommentTxt;
        this.updDtm = updDtm;
        this.updUserId = updUserId;
    }

    public EfilingFilingPackage() {

    }
}
