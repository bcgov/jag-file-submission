package ca.bc.gov.open.jag.packagereview.model;

import org.joda.time.DateTime;


public class FilingPackage {
    private String clientFileNo;
    private Boolean existingCourtFileYN;
    private String filingCommentsTxt;
    private String firstName;
    private Boolean hasChecklist;
    private Boolean hasRegistryNotice;
    private String lastName;
    private String packageNo;
    private DateTime submittedDate;
    private Court court;

    public String getClientFileNo() {
        return clientFileNo;
    }

    public void setClientFileNo(String clientFileNo) {
        this.clientFileNo = clientFileNo;
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

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }
}
