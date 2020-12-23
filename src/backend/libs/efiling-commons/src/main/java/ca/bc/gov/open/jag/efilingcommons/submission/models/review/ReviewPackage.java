package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import ca.bc.gov.open.jag.efilingcommons.model.Party;
import org.joda.time.DateTime;


import java.util.List;

public class ReviewPackage {
    private String clientFileNo;
    private DateTime submittedDate;
    private String filingCommentsTxt;
    private String firstName;
    private Boolean hasChecklist;
    private Boolean hasRegistryNotice;
    private String lastName;
    private String packageNo;
    private ReviewCourt reviewCourt;
    private ReviewProcessRequest procRequest;
    private List<ReviewDocument> files;
    private List<ReviewPackageRequest> packageRequests;
    private List<Party> parties;
    private List<PackagePayment> payments;

    public String getClientFileNo() {
        return clientFileNo;
    }

    public void setClientFileNo(String clientFileNo) {
        this.clientFileNo = clientFileNo;
    }

    public DateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(DateTime submittedDate) {
        this.submittedDate = submittedDate;
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

    public ReviewCourt getReviewCourt() {
        return reviewCourt;
    }

    public void setReviewCourt(ReviewCourt reviewCourt) {
        this.reviewCourt = reviewCourt;
    }

    public ReviewProcessRequest getProcRequest() {
        return procRequest;
    }

    public void setProcRequest(ReviewProcessRequest procRequest) {
        this.procRequest = procRequest;
    }

    public List<ReviewDocument> getFiles() {
        return files;
    }

    public void setFiles(List<ReviewDocument> files) {
        this.files = files;
    }

    public List<ReviewPackageRequest> getPackageRequests() {
        return packageRequests;
    }

    public void setPackageRequests(List<ReviewPackageRequest> packageRequests) {
        this.packageRequests = packageRequests;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    public List<PackagePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<PackagePayment> payments) {
        this.payments = payments;
    }
}
