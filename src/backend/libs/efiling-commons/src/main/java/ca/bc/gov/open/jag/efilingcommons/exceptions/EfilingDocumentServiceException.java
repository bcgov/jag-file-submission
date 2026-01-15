package ca.bc.gov.open.jag.efilingcommons.exceptions;

public class EfilingDocumentServiceException extends RuntimeException {

    public EfilingDocumentServiceException(String message) {
        super(message);
    }

    public EfilingDocumentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
