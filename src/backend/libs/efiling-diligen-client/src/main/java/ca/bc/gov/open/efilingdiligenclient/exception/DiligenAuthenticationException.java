package ca.bc.gov.open.efilingdiligenclient.exception;

public class DiligenAuthenticationException extends RuntimeException {

    public DiligenAuthenticationException(String message) {
        super(message);
    }

    public DiligenAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
