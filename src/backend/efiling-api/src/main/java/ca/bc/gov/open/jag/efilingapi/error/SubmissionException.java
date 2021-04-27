package ca.bc.gov.open.jag.efilingapi.error;

public class SubmissionException extends EfilingException {

    public SubmissionException(String message) {
        super(message, ErrorCode.SUBMISSION_FAILURE);
    }
}
