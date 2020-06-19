package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

public class DocumentProperties {

    private String type;
    private String subType;
    private SubmissionAccess submissionAccess;

    public DocumentProperties(String type, String subType, SubmissionAccess submissionAccess) {
        this.type = type;
        this.subType = subType;
        this.submissionAccess = submissionAccess;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public SubmissionAccess getSubmissionAccess() {
        return submissionAccess;
    }

    public void setSubmissionAccess(SubmissionAccess submissionAccess) {
        this.submissionAccess = submissionAccess;
    }
}
