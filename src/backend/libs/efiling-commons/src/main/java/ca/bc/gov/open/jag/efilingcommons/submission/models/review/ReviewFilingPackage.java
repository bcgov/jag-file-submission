package ca.bc.gov.open.jag.efilingcommons.submission.models.review;

import ca.bc.gov.open.jag.efilingcommons.model.Court;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReviewFilingPackage extends FilingPackage {
    private String clientFileNo;
    private Boolean existingCourtFileYN;
    private String filingCommentsTxt;
    private String firstName;
    private Boolean hasChecklist;
    private Boolean hasRegistryNotice;
    private String lastName;
    private String packageNo;
    private List<ReviewPackageRequest> packageRequests;
    private List<PackagePayment> payments;
    //TODO: protected RushOrderRequest procRequest;
    private DateTime submittedDate;

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

    public List<ReviewPackageRequest> getPackageRequests() {
        return packageRequests;
    }

    public void setPackageRequests(List<ReviewPackageRequest> packageRequests) {
        this.packageRequests = packageRequests;
    }

    public List<PackagePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<PackagePayment> payments) {
        this.payments = payments;
    }

    public DateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(DateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

    public ReviewFilingPackage(BigDecimal submissionFeeAmount, Court court, List<Document> documents, List<Party> parties, boolean rushedSubmission, String applicationCode,
                               @JsonProperty("clientFileNo") String clientFileNo,
                               @JsonProperty("existingCourtFileYN") Boolean existingCourtFileYN,
                               @JsonProperty("filingCommentsTxt") String filingCommentsTxt,
                               @JsonProperty("firstName") String firstName,
                               @JsonProperty("hasChecklist") Boolean hasChecklist,
                               @JsonProperty("hasRegistryNotice") Boolean hasRegistryNotice,
                               @JsonProperty("lastName") String lastName,
                               @JsonProperty("packageNo") String packageNo,
                               @JsonProperty("packageRequests") List<ReviewPackageRequest> packageRequests,
                               @JsonProperty("payments") List<PackagePayment> payments,
                               @JsonProperty("submittedDate") DateTime submittedDate) {
        super(submissionFeeAmount, court, documents, parties, rushedSubmission, applicationCode);
        this.clientFileNo = clientFileNo;
        this.existingCourtFileYN = existingCourtFileYN;
        this.filingCommentsTxt = filingCommentsTxt;
        this.firstName = firstName;
        this.hasChecklist = hasChecklist;
        this.hasRegistryNotice = hasRegistryNotice;
        this.lastName = lastName;
        this.packageNo = packageNo;
        this.packageRequests = packageRequests;
        this.payments = payments;
        this.submittedDate = submittedDate;
    }

    public ReviewFilingPackage(Builder builder) {
        super(builder);
        this.clientFileNo = builder.clientFileNo;
        this.existingCourtFileYN = builder.existingCourtFileYN;
        this.filingCommentsTxt = builder.filingCommentsTxt;
        this.firstName = builder.firstName;
        this.hasChecklist = builder.hasChecklist;
        this.hasRegistryNotice = builder.hasRegistryNotice;
        this.lastName = builder.lastName;
        this.packageNo = builder.packageNo;
        this.packageRequests.addAll(builder.packageRequests);
        this.payments.addAll(builder.payments);
        this.submittedDate = builder.submittedDate;
    }

    public static class Builder extends FilingPackage.Builder {

        public Builder() {}

        private String clientFileNo;
        private Boolean existingCourtFileYN;
        private String filingCommentsTxt;
        private String firstName;
        private Boolean hasChecklist;
        private Boolean hasRegistryNotice;
        private String lastName;
        private String packageNo;
        private List<ReviewPackageRequest> packageRequests = new ArrayList<>();
        private List<PackagePayment> payments = new ArrayList<>();
        //TODO: protected RushOrderRequest procRequest;
        private DateTime submittedDate;


        public ReviewFilingPackage.Builder clientFileNo(String clientFileNo) {
            this.clientFileNo = clientFileNo;
            return this;
        }

        public ReviewFilingPackage.Builder existingCourtFileYN(Boolean existingCourtFileYN) {
            this.existingCourtFileYN = existingCourtFileYN;
            return this;
        }

        public ReviewFilingPackage.Builder filingCommentsTxt(String filingCommentsTxt) {
            this.filingCommentsTxt = filingCommentsTxt;
            return this;
        }

        public ReviewFilingPackage.Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ReviewFilingPackage.Builder hasChecklist(Boolean hasChecklist) {
            this.hasChecklist = hasChecklist;
            return this;
        }

        public ReviewFilingPackage.Builder hasRegistryNotice(Boolean hasRegistryNotice) {
            this.hasRegistryNotice = hasRegistryNotice;
            return this;
        }

        public ReviewFilingPackage.Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ReviewFilingPackage.Builder packageNo(String packageNo) {
            this.packageNo = packageNo;
            return this;
        }

        public ReviewFilingPackage.Builder packageRequests(List<ReviewPackageRequest> packageRequests) {
            this.packageRequests = packageRequests;
            return this;
        }

        public ReviewFilingPackage.Builder payments(List<PackagePayment> payments) {
            this.payments = payments;
            return this;
        }

        public ReviewFilingPackage.Builder payments(DateTime submittedDate) {
            this.submittedDate = submittedDate;
            return this;
        }

        public ReviewFilingPackage create() {
            return new ReviewFilingPackage(this);
        }
    }
}
