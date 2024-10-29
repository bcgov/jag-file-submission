package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import ca.bc.gov.open.jag.efilingcommons.model.Individual;
import ca.bc.gov.open.jag.efilingcommons.model.Organization;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReviewFilingPackage {

    private String clientFileNo;
    private String status;
    private BigDecimal submissionFeeAmount;
    private ReviewCourt court;
    private List<ReviewDocument> documents = new ArrayList<>();
    private List<Individual> parties = new ArrayList<>();
    private List<Organization> organizations = new ArrayList<>();
    private boolean rushedSubmission = false;
    private String applicationCode;
    private Boolean existingCourtFileYN;
    private String filingCommentsTxt;
    private String firstName;
    private Boolean hasChecklist;
    private Boolean hasRegistryNotice;
    private String lastName;
    private String packageNo;
    private List<ReviewPackageRequest> packageRequests;
    private List<PackagePayment> payments;
    private ReviewRushOrder rushOrder;
    private DateTime submittedDate;
    private PackageLinks packageLinks;

    public ReviewFilingPackage() { }

    public BigDecimal getSubmissionFeeAmount() {
        return submissionFeeAmount;
    }

    public ReviewCourt getCourt() {
        return court;
    }

    public List<ReviewDocument> getDocuments() {
        return documents;
    }

    public List<Individual> getParties() {
        return parties;
    }

    public boolean isRushedSubmission() {
        return rushedSubmission;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public String getClientFileNo() {
        return clientFileNo;
    }

    public Boolean getExistingCourtFileYN() {
        return existingCourtFileYN;
    }

    public String getFilingCommentsTxt() {
        return filingCommentsTxt;
    }

    public String getFirstName() {
        return firstName;
    }

    public Boolean getHasChecklist() {
        return hasChecklist;
    }

    public Boolean getHasRegistryNotice() {
        return hasRegistryNotice;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public List<ReviewPackageRequest> getPackageRequests() {
        return packageRequests;
    }

    public List<PackagePayment> getPayments() {
        return payments;
    }

    public DateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setClientFileNo(String clientFileNo) {
        this.clientFileNo = clientFileNo;
    }

    public void setSubmissionFeeAmount(BigDecimal submissionFeeAmount) {
        this.submissionFeeAmount = submissionFeeAmount;
    }

    public void setCourt(ReviewCourt court) {
        this.court = court;
    }

    public void setDocuments(List<ReviewDocument> documents) {
        this.documents = documents;
    }

    public void setParties(List<Individual> parties) {
        this.parties = parties;
    }

    public void setRushedSubmission(boolean rushedSubmission) {
        this.rushedSubmission = rushedSubmission;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public void setExistingCourtFileYN(Boolean existingCourtFileYN) {
        this.existingCourtFileYN = existingCourtFileYN;
    }

    public void setFilingCommentsTxt(String filingCommentsTxt) {
        this.filingCommentsTxt = filingCommentsTxt;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setHasChecklist(Boolean hasChecklist) {
        this.hasChecklist = hasChecklist;
    }

    public void setHasRegistryNotice(Boolean hasRegistryNotice) {
        this.hasRegistryNotice = hasRegistryNotice;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public void setPackageRequests(List<ReviewPackageRequest> packageRequests) {
        this.packageRequests = packageRequests;
    }

    public void setPayments(List<PackagePayment> payments) {
        this.payments = payments;
    }

    public void setSubmittedDate(DateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

    public PackageLinks getPackageLinks() {
        return packageLinks;
    }

    public void setPackageLinks(PackageLinks packageLinks) {
        this.packageLinks = packageLinks;
    }

    public List<Organization> getOrganizations() { return organizations; }

    public void setOrganizations(List<Organization> organizations) { this.organizations = organizations; }

    public ReviewRushOrder getRushOrder() { return rushOrder;  }

    public void setRushOrder(ReviewRushOrder rushOrder) {  this.rushOrder = rushOrder; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
