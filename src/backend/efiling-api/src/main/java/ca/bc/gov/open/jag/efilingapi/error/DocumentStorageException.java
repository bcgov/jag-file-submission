package ca.bc.gov.open.jag.efilingapi.error;

public class DocumentStorageException extends EfilingException {

    public DocumentStorageException(String message) {
        super(message, ErrorCode.DOCUMENT_STORAGE_FAILURE);
    }
}
