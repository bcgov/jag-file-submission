package ca.bc.gov.open.jag.efilingreviewerapi.error;

public class AiReviewerDocumentConfigException extends AiReviewerException {

    public AiReviewerDocumentConfigException(String message) {
        super(message, ErrorCode.DOCUMENT_CONFIGURATION_NOT_FOUND);
    }


}
