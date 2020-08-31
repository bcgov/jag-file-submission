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

    public FilingPackage(
            @JsonProperty("submissionFeeAmount") BigDecimal submissionFeeAmount,
            @JsonProperty("court") Court court,
            @JsonProperty("documents") List<Document> documents,
            @JsonProperty("parties") List<Party> parties) {

        this.submissionFeeAmount = submissionFeeAmount;
        this.court = court;
        this.documents.addAll(documents);
        this.parties.addAll(parties);
    }

    public FilingPackage(Builder builder) {
        this.submissionFeeAmount = builder.submissionFeeAmount;
        this.court = builder.court;
        this.documents.addAll(builder.documents);
        this.parties.addAll(builder.parties);
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


