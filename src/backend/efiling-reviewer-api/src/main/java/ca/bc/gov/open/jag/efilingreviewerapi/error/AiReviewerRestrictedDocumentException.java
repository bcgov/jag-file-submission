package ca.bc.gov.open.jag.efilingreviewerapi.error;

public class AiReviewerRestrictedDocumentException extends AiReviewerException {
    public AiReviewerRestrictedDocumentException(String message) { super(message, ErrorCode.RESTRICTED_DOCUMENT); }
}
