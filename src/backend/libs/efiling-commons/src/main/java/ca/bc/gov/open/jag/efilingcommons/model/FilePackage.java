package ca.bc.gov.open.jag.efilingcommons.model;

import org.joda.time.DateTime;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class FilePackage {
    private String clientFileNo;
    private String courtClassCd;
    private String courtFileNo;
    private String courtLevelCd;
    private String courtLocationCd;
    private BigDecimal courtLocationId;
    private String courtLocationName;
    private Boolean existingCourtFileYN;
    private String filingCommentsTxt;
    private String firstName;
    private Boolean hasChecklist;
    private Boolean hasRegistryNotice;
    private String lastName;
    private String packageNo;
    private DateTime submittedDate;

    public String getClientFileNo() {
        return clientFileNo;
    }

    public void setClientFileNo(String clientFileNo) {
        this.clientFileNo = clientFileNo;
    }

    public String getCourtClassCd() {
        return courtClassCd;
    }

    public void setCourtClassCd(String courtClassCd) {
        this.courtClassCd = courtClassCd;
    }

    public String getCourtFileNo() {
        return courtFileNo;
    }

    public void setCourtFileNo(String courtFileNo) {
        this.courtFileNo = courtFileNo;
    }

    public String getCourtLevelCd() {
        return courtLevelCd;
    }

    public void setCourtLevelCd(String courtLevelCd) {
        this.courtLevelCd = courtLevelCd;
    }

    public String getCourtLocationCd() {
        return courtLocationCd;
    }

    public void setCourtLocationCd(String courtLocationCd) {
        this.courtLocationCd = courtLocationCd;
    }

    public BigDecimal getCourtLocationId() {
        return courtLocationId;
    }

    public void setCourtLocationId(BigDecimal courtLocationId) {
        this.courtLocationId = courtLocationId;
    }

    public String getCourtLocationName() {
        return courtLocationName;
    }

    public void setCourtLocationName(String courtLocationName) {
        this.courtLocationName = courtLocationName;
    }

    public Boolean getExistingCourtFileYN() {
        return existingCourtFileYN;
    }

    public void setExistingCourtFileYN(Boolean existingCourtFileYN) {
        this.existingCourtFileYN = existingCourtFileYN;
    }

    public String getFilingCommentsTxt() {
        return filingCommentsTxt;
    }

    public void setFilingCommentsTxt(String filingCommentsTxt) {
        this.filingCommentsTxt = filingCommentsTxt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getHasChecklist() {
        return hasChecklist;
    }

    public void setHasChecklist(Boolean hasChecklist) {
        this.hasChecklist = hasChecklist;
    }

    public Boolean getHasRegistryNotice() {
        return hasRegistryNotice;
    }

    public void setHasRegistryNotice(Boolean hasRegistryNotice) {
        this.hasRegistryNotice = hasRegistryNotice;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public DateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(DateTime submittedDate) {
        this.submittedDate = submittedDate;
    }
}
