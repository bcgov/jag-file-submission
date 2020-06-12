package ca.bc.gov.open.jagefilingapi.submission.models;

import ca.bc.gov.open.api.model.DocumentProperties;
import ca.bc.gov.open.api.model.Navigation;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the submission details for a transaction
 */
public class Submission {

    private DocumentProperties documentProperties;

    private Navigation navigation;

    private Fee fee;

    protected Submission(Submission.Builder builder) {
        this.documentProperties = builder.documentProperties;
        this.navigation = builder.navigation;
        this.fee = builder.fee;
    }

    public static Submission.Builder builder() {
        return new Submission.Builder();
    }

    @JsonCreator
    public Submission(
            @JsonProperty("submissionMetadata") DocumentProperties documentProperties,
            @JsonProperty("navigation") Navigation navigation,
            @JsonProperty("fee") Fee fee) {
        this.documentProperties = documentProperties;
        this.navigation = navigation;
        this.fee = fee;
    }

    public DocumentProperties getDocumentProperties() {
        return documentProperties;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Fee getFee() {
        return fee;
    }

    public static class Builder {

        private DocumentProperties documentProperties;
        private Navigation navigation;
        private Fee fee;

        public Builder documentProperties(DocumentProperties documentProperties) {
            this.documentProperties =  documentProperties;
            return this;
        }

        public Builder navigation(Navigation navigation) {
            this.navigation = navigation;
            return this;
        }

        public Builder fee(Fee fee) {
            this.fee = fee;
            return this;
        }

        public Submission create() {
            return new Submission(this);
        }
    }


}
