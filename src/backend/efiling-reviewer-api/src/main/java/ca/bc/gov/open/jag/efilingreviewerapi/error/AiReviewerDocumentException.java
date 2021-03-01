package ca.bc.gov.open.jag.efilingreviewerapi.error;

public class AiReviewerDocumentException extends AiReviewerException {
    public AiReviewerDocumentException(String message) { super(message, ErrorCode.DOCUMENT_VALIDATION); }
}
