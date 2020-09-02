package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FilingPackage {

    private BigDecimal submissionFeeAmount;
    private Court court;
    private List<Document> documents = new ArrayList<>();
    private List<Party> parties = new ArrayList<>();
    private String applicationType;
    private boolean rushedSubmission = false;

    public FilingPackage(
            @JsonProperty("submissionFeeAmount") BigDecimal submissionFeeAmount,
            @JsonProperty("court") Court court,
            @JsonProperty("documents") List<Document> documents,
            @JsonProperty("parties") List<Party> parties,
            @JsonProperty("applicationType") String applicationType,
            @JsonProperty("rushedSubmission") boolean rushedSubmission) {

        this.submissionFeeAmount = submissionFeeAmount;
        this.court = court;
        this.applicationType = applicationType;
        this.rushedSubmission = rushedSubmission;
        this.documents.addAll(documents);
        this.parties.addAll(parties);
    }

    public FilingPackage(Builder builder) {
        this.submissionFeeAmount = builder.submissionFeeAmount;
        this.court = builder.court;
        this.documents.addAll(builder.documents);
        this.parties.addAll(builder.parties);
        this.rushedSubmission = builder.rushedSubmission;
        this.applicationType = builder.applicationType;
    }

    public BigDecimal getSubmissionFeeAmount() {
        return submissionFeeAmount;
    }

    public Court getCourt() {
        return court;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Party> getParties() {
        return parties;
    }

    public String getApplicationType() { return applicationType; }

    public boolean isRushedSubmission() { return rushedSubmission; }

    public static Builder builder() {
        return new Builder();
    }

    public void addDocument(Document document) {
        if(this.documents == null) this.documents = new ArrayList<>();
        this.documents.add(document);
    }

    public static class Builder {

        private BigDecimal submissionFeeAmount;
        private Court court;
        private List<Document> documents = new ArrayList<>();
        private List<Party> parties = new ArrayList<>();
        private String applicationType;
        private boolean rushedSubmission;

        public Builder rushedSubmission(boolean rushedSubmission) {
            this.rushedSubmission = rushedSubmission;
            return this;
        }

        public Builder applicationType(String applicationType) {
            this.applicationType = applicationType;
            return this;
        }

        public Builder submissionFeeAmount(BigDecimal submissionFeeAmount) {
            this.submissionFeeAmount = submissionFeeAmount;
            return this;
        }

        public Builder court(Court court) {
            this.court = court;
            return this;
        }

        public Builder documents(List<Document> documents) {
            this.documents = documents;
            return this;
        }

        public Builder parties(List<Party> parties) {
            this.parties = parties;
            return this;
        }

        public FilingPackage create() {
            return new FilingPackage(this);
        }
    }
}

