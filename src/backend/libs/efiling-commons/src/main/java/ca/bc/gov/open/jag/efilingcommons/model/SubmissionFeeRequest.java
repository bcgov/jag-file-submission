package ca.bc.gov.open.jag.efilingcommons.model;

public class SubmissionFeeRequest {
    private String serviceType;
    private String application;
    private String division;
    private String level;
    private String classification;


    public SubmissionFeeRequest(SubmissionFeeRequest.Builder builder) {
        this.serviceType = builder.serviceType;
        this.application = builder.application;
        this.division = builder.division;
        this.level = builder.level;
        this.classification = builder.classification;
    }


    public String getServiceType() {
        return serviceType;
    }

    public String getApplication() {
        return application;
    }

    public String getDivision() {
        return division;
    }

    public String getLevel() {
        return level;
    }

    public String getClassification() {
        return classification;
    }

    public static SubmissionFeeRequest.Builder builder() {
        return new SubmissionFeeRequest.Builder();
    }

    public static class Builder {

        private String serviceType;
        private String application;
        private String division;
        private String level;
        private String classification;

        public SubmissionFeeRequest.Builder serviceType(String serviceType) { this.serviceType = serviceType; return this;}
        public SubmissionFeeRequest.Builder application(String application) { this.application = application; return this;}
        public SubmissionFeeRequest.Builder division(String division) { this.division = division; return this;}
        public SubmissionFeeRequest.Builder level(String level) { this.level = level; return this;}
        public SubmissionFeeRequest.Builder classification(String classification) { this.classification = classification; return this;}

        public SubmissionFeeRequest create() {
            return new SubmissionFeeRequest(this);
        }

    }
}
