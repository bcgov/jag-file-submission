package ca.bc.gov.open.jag.efilingapi.error;

public class InvalidInitialSubmissionPayloadException extends EfilingException {
    public InvalidInitialSubmissionPayloadException(String message) {
        super(message, ErrorCode.INVALID_INITIAL_SUBMISSION_PAYLOAD);
    }
}
