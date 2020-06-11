package ca.bc.gov.open.jagefilingapi.submission.models;

import ca.bc.gov.open.api.model.Navigation;
import ca.bc.gov.open.api.model.SubmissionMetadata;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the submission details for a transaction
 */
public class Submission {

    private SubmissionMetadata submissionMetadata;

    private Navigation navigation;

    private Fee fee;

    protected Submission(Submission.Builder builder) {
        this.submissionMetadata = builder.submissionMetadata;
        this.navigation = builder.navigation;
        this.fee = builder.fee;
    }

    public static Submission.Builder builder() {
        return new Submission.Builder();
    }

    @JsonCreator
    public Submission(
            @JsonProperty("submissionMetadata") SubmissionMetadata submissionMetadata,
            @JsonProperty("navigation") Navigation navigation,
            @JsonProperty("fee") Fee fee) {
        this.submissionMetadata = submissionMetadata;
        this.navigation = navigation;
        this.fee = fee;
    }

    public SubmissionMetadata getSubmissionMetadata() {
        return submissionMetadata;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Fee getFee() {
        return fee;
    }

    public static class Builder {

        private SubmissionMetadata submissionMetadata;
        private Navigation navigation;
        private Fee fee;

        public Builder submissionMetadata(SubmissionMetadata submissionMetadata) {
            this.submissionMetadata =  submissionMetadata;
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
