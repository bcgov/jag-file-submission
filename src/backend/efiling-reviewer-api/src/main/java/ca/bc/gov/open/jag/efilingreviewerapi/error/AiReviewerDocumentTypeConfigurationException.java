package ca.bc.gov.open.jag.efilingreviewerapi.error;

public class AiReviewerDocumentTypeConfigurationException extends AiReviewerException {
    public AiReviewerDocumentTypeConfigurationException(String message) { super(message, ErrorCode.DOCUMENT_CONFIGURATION_MANAGEMENT); }
}
