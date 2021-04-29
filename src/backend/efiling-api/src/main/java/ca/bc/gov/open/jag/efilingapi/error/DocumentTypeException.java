package ca.bc.gov.open.jag.efilingapi.error;

public class DocumentTypeException extends EfilingException {

    public DocumentTypeException(String message) {
        super(message, ErrorCode.DOCUMENT_TYPE_ERROR);
    }
}
