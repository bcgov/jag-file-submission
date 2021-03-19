package ca.bc.gov.open.jag.efilingreviewerapi.error;

public class AiReviewerDocumentTypeMismatchException extends AiReviewerException {
    public AiReviewerDocumentTypeMismatchException(String message) { super(message, ErrorCode.DOCUMENT_TYPE_MISMATCH); }
}
