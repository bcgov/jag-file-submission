package ca.bc.gov.open.jag.efilingapi.error;

public class MissingUniversalIdException extends EfilingException {
    public MissingUniversalIdException(String message) {
        super(message, ErrorCode.MISSING_UNIVERSAL_ID);
    }
}
