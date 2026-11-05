package ca.bc.gov.open.jag.efilingapi.error;

import java.util.List;

public class InvalidInitialSubmissionPayloadException extends EfilingException {

    private final List<String> details;

    public InvalidInitialSubmissionPayloadException(String message, List<String> details) {
        super(message, ErrorCode.INVALID_INITIAL_SUBMISSION_PAYLOAD);
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }
}
