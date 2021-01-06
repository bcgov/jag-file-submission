package ca.bc.gov.open.jag.efiling.error;

public class EfilingTestException extends RuntimeException {

    public EfilingTestException(String message) {
        super(message);
    }

    public EfilingTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
