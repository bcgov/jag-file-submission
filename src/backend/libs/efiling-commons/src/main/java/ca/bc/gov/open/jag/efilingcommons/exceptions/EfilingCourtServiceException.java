package ca.bc.gov.open.jag.efilingcommons.exceptions;

public class EfilingCourtServiceException extends RuntimeException {

    public EfilingCourtServiceException(String message) {
        super(message);
    }

    public EfilingCourtServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
