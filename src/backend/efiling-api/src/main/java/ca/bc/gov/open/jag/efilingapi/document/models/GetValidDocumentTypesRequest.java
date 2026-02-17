package ca.bc.gov.open.jag.efilingapi.document.models;

public class GetValidDocumentTypesRequest {
    private String courtLevel;
    private String courtClassification;

    public String getCourtLevel() {
        return courtLevel;
    }

    public String getCourtClassification() {
        return courtClassification;
    }

    public GetValidDocumentTypesRequest(Builder builder) {
        this.courtLevel = builder.courtLevel;
        this.courtClassification = builder.courtClassification;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String courtLevel;
        private String courtClassification;

        public Builder courtClassification(String courtClassification) {
            this.courtClassification = courtClassification;
            return this;
        }

        public Builder courtLevel(String courtLevel) {
            this.courtLevel = courtLevel;
            return this;
        }

        public GetValidDocumentTypesRequest create() {
            return new GetValidDocumentTypesRequest(this);
        }

    }

}
