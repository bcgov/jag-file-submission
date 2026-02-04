package ca.bc.gov.open.jag.efilingapi.error;

public class DocumentRequiredException extends EfilingException {

    public DocumentRequiredException(String message) {
        super(message, ErrorCode.DOCUMENT_REQUIRED);
    }
}
