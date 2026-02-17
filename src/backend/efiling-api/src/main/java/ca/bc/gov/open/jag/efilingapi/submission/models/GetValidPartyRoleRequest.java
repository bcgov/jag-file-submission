package ca.bc.gov.open.jag.efilingapi.submission.models;


import ca.bc.gov.open.jag.efilingapi.api.model.InitialDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetValidPartyRoleRequest {

    private List<InitialDocument> initialDocuments = new ArrayList<>();
    private String courtLevel;
    private String courtClassification;

    private String division;

    public List<InitialDocument> getInitialDocuments() {
        return initialDocuments;
    }

    public String getCourtLevel() {
        return courtLevel;
    }

    public String getCourtClassification() {
        return courtClassification;
    }

    public String getDivision() {
        return division;
    }

    public String getDocumentTypesAsString() {
        return this.initialDocuments.stream().map(InitialDocument::getType).collect(Collectors.joining(","));
    }

    public GetValidPartyRoleRequest(Builder builder) {
        this.initialDocuments = builder.documents;
        this.courtLevel = builder.courtLevel;
        this.division = builder.division;
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
        private String division;

        public Builder courtLevel(String courtLevel) {
            this.courtLevel = courtLevel;
            return this;
        }

        private String courtClassification;

        public Builder courtClassification(String courtClassification) {
            this.courtClassification = courtClassification;
            return this;
        }

        public Builder division(String division) {
            this.division = division;
            return this;
        }

        private List<InitialDocument> documents;

        public Builder documents(List<InitialDocument> documents) {
            this.documents = documents;
            return this;
        }

    }

}
