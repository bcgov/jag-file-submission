package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public class ReviewRushOrder {
    protected DateTime courtOrderDt;
    protected BigDecimal packageId;
    protected String rushFilingReasonTxt;
    protected String contactEmailTxt;
    protected String contactFirstGivenNm;
    protected String contactOrganizationNm;
    protected String contactPhoneNo;
    protected String contactSurnameNm;
    protected BigDecimal ctryId;
    protected String countryDsc;
    protected String currentStatusDsc;
    protected String processReasonCd;
    protected String processingCommentTxt;
    protected List<RushDocument> supportDocs;

    public ReviewRushOrder() {}

    public DateTime getCourtOrderDt() {
        return courtOrderDt;
    }

    public void setCourtOrderDt(DateTime courtOrderDt) {
        this.courtOrderDt = courtOrderDt;
    }

    public BigDecimal getPackageId() {
        return packageId;
    }

    public void setPackageId(BigDecimal packageId) {
        this.packageId = packageId;
    }

    public String getRushFilingReasonTxt() {
        return rushFilingReasonTxt;
    }

    public void setRushFilingReasonTxt(String rushFilingReasonTxt) {
        this.rushFilingReasonTxt = rushFilingReasonTxt;
    }

    public String getContactEmailTxt() { return contactEmailTxt;  }

    public void setContactEmailTxt(String contactEmailTxt) { this.contactEmailTxt = contactEmailTxt; }

    public String getContactFirstGivenNm() { return contactFirstGivenNm; }

    public void setContactFirstGivenNm(String contactFirstGivenNm) { this.contactFirstGivenNm = contactFirstGivenNm; }

    public String getContactOrganizationNm() { return contactOrganizationNm; }

    public void setContactOrganizationNm(String contactOrganizationNm) { this.contactOrganizationNm = contactOrganizationNm;  }

    public String getContactPhoneNo() { return contactPhoneNo; }

    public void setContactPhoneNo(String contactPhoneNo) { this.contactPhoneNo = contactPhoneNo; }

    public String getContactSurnameNm() { return contactSurnameNm; }

    public void setContactSurnameNm(String contactSurnameNm) { this.contactSurnameNm = contactSurnameNm; }

    public BigDecimal getCtryId() { return ctryId; }

    public void setCtryId(BigDecimal ctryId) { this.ctryId = ctryId;  }

    public String getCountryDsc() { return countryDsc; }

    public void setCountryDsc(String countryDsc) { this.countryDsc = countryDsc; }

    public String getCurrentStatusDsc() { return currentStatusDsc;  }

    public void setCurrentStatusDsc(String currentStatusDsc) { this.currentStatusDsc = currentStatusDsc; }

    public String getProcessReasonCd() { return processReasonCd;  }

    public void setProcessReasonCd(String processReasonCd) { this.processReasonCd = processReasonCd;  }

    public String getProcessingCommentTxt() { return processingCommentTxt; }

    public void setProcessingCommentTxt(String processingCommentTxt) { this.processingCommentTxt = processingCommentTxt; }

    public List<RushDocument> getSupportDocs() {
        return supportDocs;
    }

    public void setSupportDocs(List<RushDocument> supportDocs) {
        this.supportDocs = supportDocs;
    }
}
