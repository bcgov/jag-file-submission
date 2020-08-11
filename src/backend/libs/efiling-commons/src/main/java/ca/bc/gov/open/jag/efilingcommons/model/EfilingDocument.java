package ca.bc.gov.open.jag.efilingcommons.model;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingDocument {
    private Boolean amendsAnotherDocumentYn;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar appearanceDt;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar appearanceTm;
    private BigDecimal appearnceAgenId;
    private BigDecimal bailiffId;
    private String clientFileNameTxt;
    private String conferenceRequestTypeCd;
    private String documentDescriptionTxt;
    private BigDecimal documentId;
    private String documentSubtypeCd;
    private String documentTypeCd;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar entDtm;
    private String entUserId;
    private BigDecimal estimatedToHearDocHrsQty;
    private BigDecimal estimatedToHearDocMinsQty;
    private String expertWitnessCallModeCd;
    private Boolean feeExemptYn;
    private String filePath;
    private String fileServer;
    private String filingBodyCd;
    private String largeFileTimeMilliSecs;
    private String largeFileYn;
    private Boolean mediationStepYn;
    private BigDecimal packageId;
    private BigDecimal packageSeqNo;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar updDtm;
    private String updUserId;
    private String uploadStateCd;
    private String uploadedToApplicationCd;
    private Boolean xmlDataCompleteYn;
    private Boolean xmlDocumentInstanceYn;
    private Boolean xmlValidYn;

    public Boolean getAmendsAnotherDocumentYn() {
        return amendsAnotherDocumentYn;
    }

    public void setAmendsAnotherDocumentYn(Boolean amendsAnotherDocumentYn) {
        this.amendsAnotherDocumentYn = amendsAnotherDocumentYn;
    }

    public XMLGregorianCalendar getAppearanceDt() {
        return appearanceDt;
    }

    public void setAppearanceDt(XMLGregorianCalendar appearanceDt) {
        this.appearanceDt = appearanceDt;
    }

    public XMLGregorianCalendar getAppearanceTm() {
        return appearanceTm;
    }

    public void setAppearanceTm(XMLGregorianCalendar appearanceTm) {
        this.appearanceTm = appearanceTm;
    }

    public BigDecimal getAppearnceAgenId() {
        return appearnceAgenId;
    }

    public void setAppearnceAgenId(BigDecimal appearnceAgenId) {
        this.appearnceAgenId = appearnceAgenId;
    }

    public BigDecimal getBailiffId() {
        return bailiffId;
    }

    public void setBailiffId(BigDecimal bailiffId) {
        this.bailiffId = bailiffId;
    }

    public String getClientFileNameTxt() {
        return clientFileNameTxt;
    }

    public void setClientFileNameTxt(String clientFileNameTxt) {
        this.clientFileNameTxt = clientFileNameTxt;
    }

    public String getConferenceRequestTypeCd() {
        return conferenceRequestTypeCd;
    }

    public void setConferenceRequestTypeCd(String conferenceRequestTypeCd) {
        this.conferenceRequestTypeCd = conferenceRequestTypeCd;
    }

    public String getDocumentDescriptionTxt() {
        return documentDescriptionTxt;
    }

    public void setDocumentDescriptionTxt(String documentDescriptionTxt) {
        this.documentDescriptionTxt = documentDescriptionTxt;
    }

    public BigDecimal getDocumentId() {
        return documentId;
    }

    public void setDocumentId(BigDecimal documentId) {
        this.documentId = documentId;
    }

    public String getDocumentSubtypeCd() {
        return documentSubtypeCd;
    }

    public void setDocumentSubtypeCd(String documentSubtypeCd) {
        this.documentSubtypeCd = documentSubtypeCd;
    }

    public String getDocumentTypeCd() {
        return documentTypeCd;
    }

    public void setDocumentTypeCd(String documentTypeCd) {
        this.documentTypeCd = documentTypeCd;
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

    public BigDecimal getEstimatedToHearDocHrsQty() {
        return estimatedToHearDocHrsQty;
    }

    public void setEstimatedToHearDocHrsQty(BigDecimal estimatedToHearDocHrsQty) {
        this.estimatedToHearDocHrsQty = estimatedToHearDocHrsQty;
    }

    public BigDecimal getEstimatedToHearDocMinsQty() {
        return estimatedToHearDocMinsQty;
    }

    public void setEstimatedToHearDocMinsQty(BigDecimal estimatedToHearDocMinsQty) {
        this.estimatedToHearDocMinsQty = estimatedToHearDocMinsQty;
    }

    public String getExpertWitnessCallModeCd() {
        return expertWitnessCallModeCd;
    }

    public void setExpertWitnessCallModeCd(String expertWitnessCallModeCd) {
        this.expertWitnessCallModeCd = expertWitnessCallModeCd;
    }

    public Boolean getFeeExemptYn() {
        return feeExemptYn;
    }

    public void setFeeExemptYn(Boolean feeExemptYn) {
        this.feeExemptYn = feeExemptYn;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileServer() {
        return fileServer;
    }

    public void setFileServer(String fileServer) {
        this.fileServer = fileServer;
    }

    public String getFilingBodyCd() {
        return filingBodyCd;
    }

    public void setFilingBodyCd(String filingBodyCd) {
        this.filingBodyCd = filingBodyCd;
    }

    public String getLargeFileTimeMilliSecs() {
        return largeFileTimeMilliSecs;
    }

    public void setLargeFileTimeMilliSecs(String largeFileTimeMilliSecs) {
        this.largeFileTimeMilliSecs = largeFileTimeMilliSecs;
    }

    public String getLargeFileYn() {
        return largeFileYn;
    }

    public void setLargeFileYn(String largeFileYn) {
        this.largeFileYn = largeFileYn;
    }

    public Boolean getMediationStepYn() {
        return mediationStepYn;
    }

    public void setMediationStepYn(Boolean mediationStepYn) {
        this.mediationStepYn = mediationStepYn;
    }

    public BigDecimal getPackageId() {
        return packageId;
    }

    public void setPackageId(BigDecimal packageId) {
        this.packageId = packageId;
    }

    public BigDecimal getPackageSeqNo() {
        return packageSeqNo;
    }

    public void setPackageSeqNo(BigDecimal packageSeqNo) {
        this.packageSeqNo = packageSeqNo;
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

    public String getUploadStateCd() {
        return uploadStateCd;
    }

    public void setUploadStateCd(String uploadStateCd) {
        this.uploadStateCd = uploadStateCd;
    }

    public String getUploadedToApplicationCd() {
        return uploadedToApplicationCd;
    }

    public void setUploadedToApplicationCd(String uploadedToApplicationCd) {
        this.uploadedToApplicationCd = uploadedToApplicationCd;
    }

    public Boolean getXmlDataCompleteYn() {
        return xmlDataCompleteYn;
    }

    public void setXmlDataCompleteYn(Boolean xmlDataCompleteYn) {
        this.xmlDataCompleteYn = xmlDataCompleteYn;
    }

    public Boolean getXmlDocumentInstanceYn() {
        return xmlDocumentInstanceYn;
    }

    public void setXmlDocumentInstanceYn(Boolean xmlDocumentInstanceYn) {
        this.xmlDocumentInstanceYn = xmlDocumentInstanceYn;
    }

    public Boolean getXmlValidYn() {
        return xmlValidYn;
    }

    public void setXmlValidYn(Boolean xmlValidYn) {
        this.xmlValidYn = xmlValidYn;
    }

    public EfilingDocument(Boolean amendsAnotherDocumentYn, XMLGregorianCalendar appearanceDt, XMLGregorianCalendar appearanceTm, BigDecimal appearnceAgenId, BigDecimal bailiffId, String clientFileNameTxt, String conferenceRequestTypeCd, String documentDescriptionTxt, BigDecimal documentId, String documentSubtypeCd, String documentTypeCd, XMLGregorianCalendar entDtm, String entUserId, BigDecimal estimatedToHearDocHrsQty, BigDecimal estimatedToHearDocMinsQty, String expertWitnessCallModeCd, Boolean feeExemptYn, String filePath, String fileServer, String filingBodyCd, String largeFileTimeMilliSecs, String largeFileYn, Boolean mediationStepYn, BigDecimal packageId, BigDecimal packageSeqNo, XMLGregorianCalendar updDtm, String updUserId, String uploadStateCd, String uploadedToApplicationCd, Boolean xmlDataCompleteYn, Boolean xmlDocumentInstanceYn, Boolean xmlValidYn) {
        this.amendsAnotherDocumentYn = amendsAnotherDocumentYn;
        this.appearanceDt = appearanceDt;
        this.appearanceTm = appearanceTm;
        this.appearnceAgenId = appearnceAgenId;
        this.bailiffId = bailiffId;
        this.clientFileNameTxt = clientFileNameTxt;
        this.conferenceRequestTypeCd = conferenceRequestTypeCd;
        this.documentDescriptionTxt = documentDescriptionTxt;
        this.documentId = documentId;
        this.documentSubtypeCd = documentSubtypeCd;
        this.documentTypeCd = documentTypeCd;
        this.entDtm = entDtm;
        this.entUserId = entUserId;
        this.estimatedToHearDocHrsQty = estimatedToHearDocHrsQty;
        this.estimatedToHearDocMinsQty = estimatedToHearDocMinsQty;
        this.expertWitnessCallModeCd = expertWitnessCallModeCd;
        this.feeExemptYn = feeExemptYn;
        this.filePath = filePath;
        this.fileServer = fileServer;
        this.filingBodyCd = filingBodyCd;
        this.largeFileTimeMilliSecs = largeFileTimeMilliSecs;
        this.largeFileYn = largeFileYn;
        this.mediationStepYn = mediationStepYn;
        this.packageId = packageId;
        this.packageSeqNo = packageSeqNo;
        this.updDtm = updDtm;
        this.updUserId = updUserId;
        this.uploadStateCd = uploadStateCd;
        this.uploadedToApplicationCd = uploadedToApplicationCd;
        this.xmlDataCompleteYn = xmlDataCompleteYn;
        this.xmlDocumentInstanceYn = xmlDocumentInstanceYn;
        this.xmlValidYn = xmlValidYn;
    }

    public EfilingDocument() {

    }
}
