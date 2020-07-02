package ca.bc.gov.open.jag.efilingcommons.exceptions;

public class EfilingAccountServiceException extends RuntimeException {

    public EfilingAccountServiceException(String message) {
        super(message);
    }

    public EfilingAccountServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
