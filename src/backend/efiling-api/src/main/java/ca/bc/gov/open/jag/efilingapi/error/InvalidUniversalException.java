package ca.bc.gov.open.jag.efilingapi.error;

public class InvalidUniversalException extends EfilingException {
    public InvalidUniversalException(String message) {
        super(message, ErrorCode.INVALIDUNIVERSAL);
    }
}
