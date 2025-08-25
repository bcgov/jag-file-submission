package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import org.joda.time.DateTime;

public class ReviewDocument {
    private DateTime dateFiled;
    private DateTime dateWithdrawn;
    private String documentId;
    private String parentDocumentId;
    private String documentType;
    private String documentTypeCd;
    private String documentUploadStatusCd;
    private String fileName;
    private Boolean initiatingDoc;
    private String largeFileYn;
    private String packageId;
    private String packageSeqNo;
    private Boolean paymentProcessed;
    private String status;
    private String statusCode;
    private DateTime statusDate;
    private Boolean trialDivision;
    private Boolean xmlDoc;
    private Boolean rushRequired;

    public ReviewDocument() {

    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeCd() {
        return documentTypeCd;
    }

    public void setDocumentTypeCd(String documentTypeCd) {
        this.documentTypeCd = documentTypeCd;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DateTime getDateFiled() {
        return dateFiled;
    }

    public void setDateFiled(DateTime dateFiled) {
        this.dateFiled = dateFiled;
    }

    public DateTime getDateWithdrawn() {
        return dateWithdrawn;
    }

    public void setDateWithdrawn(DateTime dateWithdrawn) {
        this.dateWithdrawn = dateWithdrawn;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentUploadStatusCd() {
        return documentUploadStatusCd;
    }

    public void setDocumentUploadStatusCd(String documentUploadStatusCd) {
        this.documentUploadStatusCd = documentUploadStatusCd;
    }

    public Boolean getInitiatingDoc() {
        return initiatingDoc;
    }

    public void setInitiatingDoc(Boolean initiatingDoc) {
        this.initiatingDoc = initiatingDoc;
    }

    public String getLargeFileYn() {
        return largeFileYn;
    }

    public void setLargeFileYn(String largeFileYn) {
        this.largeFileYn = largeFileYn;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageSeqNo() {
        return packageSeqNo;
    }

    public void setPackageSeqNo(String packageSeqNo) {
        this.packageSeqNo = packageSeqNo;
    }

    public Boolean getPaymentProcessed() {
        return paymentProcessed;
    }

    public void setPaymentProcessed(Boolean paymentProcessed) {
        this.paymentProcessed = paymentProcessed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public DateTime getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(DateTime statusDate) {
        this.statusDate = statusDate;
    }

    public Boolean getTrialDivision() {
        return trialDivision;
    }

    public void setTrialDivision(Boolean trialDivision) {
        this.trialDivision = trialDivision;
    }

    public Boolean getXmlDoc() {
        return xmlDoc;
    }

    public void setXmlDoc(Boolean xmlDoc) {
        this.xmlDoc = xmlDoc;
    }

    public Boolean getRushRequired() { return rushRequired; }

    public void setRushRequired(Boolean rushRequired) { this.rushRequired = rushRequired; }

    public String getParentDocumentId() {
        return parentDocumentId;
    }

    public void setParentDocumentId(String parentDocumentId) {
        this.parentDocumentId = parentDocumentId;
    }

}
