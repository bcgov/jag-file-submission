package ca.bc.gov.open.jag.efilingapi.error;

public class MissingApplicationCodeException extends EfilingException {
    public MissingApplicationCodeException(String message) {
        super(message, ErrorCode.MISSING_APPLICATION_CODE);
    }
}
