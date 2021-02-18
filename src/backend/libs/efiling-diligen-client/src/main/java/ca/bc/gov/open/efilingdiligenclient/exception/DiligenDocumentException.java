package ca.bc.gov.open.efilingdiligenclient.exception;

public class DiligenDocumentException extends RuntimeException {

    public DiligenDocumentException(String message) {
        super(message);
    }

    public DiligenDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
