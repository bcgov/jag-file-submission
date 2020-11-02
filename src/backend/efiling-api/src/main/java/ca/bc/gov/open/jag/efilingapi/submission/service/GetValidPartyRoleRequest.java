package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetValidPartyRoleRequest {

    private List<DocumentProperties> documents = new ArrayList<>();
    private String courtLevel;
    private String courtClassification;

    public List<DocumentProperties> getDocuments() {
        return documents;
    }

    public String getCourtLevel() {
        return courtLevel;
    }

    public String getCourtClassification() {
        return courtClassification;
    }

    public String getDocumentTypeAsString() {
        return this.documents.stream().map(x -> x.getType().getValue()).collect(Collectors.joining(","));
    }

    public GetValidPartyRoleRequest(Builder builder) {
        this.documents = builder.documents;
        this.courtLevel = builder.courtLevel;
        this.courtClassification = builder.courtClassification;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public GetValidPartyRoleRequest create() {
            return new GetValidPartyRoleRequest(this);
        }

        private String courtLevel;

        public Builder courtLevel(String courtLevel) {
            this.courtLevel = courtLevel;
            return this;
        }

        private String courtClassification;

        public Builder courtClassification(String courtClassification) {
            this.courtClassification = courtClassification;
            return this;
        }

        private List<DocumentProperties> documents;

        public Builder documents(List<DocumentProperties> documents) {
            this.documents = documents;
            return this;
        }

    }

}
