package ca.bc.gov.open.jag.efilingcommons.exceptions;

public class EfilingLookupServiceException extends RuntimeException {

    public EfilingLookupServiceException(String message) {
        super(message);
    }

    public EfilingLookupServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
