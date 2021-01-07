package ca.bc.gov.open.jag.efilingcommons.exceptions;

public class EfilingStatusServiceException extends RuntimeException {

    public EfilingStatusServiceException(String message) {
        super(message);
    }

    public EfilingStatusServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
