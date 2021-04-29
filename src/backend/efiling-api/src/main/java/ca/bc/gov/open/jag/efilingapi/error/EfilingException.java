package ca.bc.gov.open.jag.efilingapi.error;

public class EfilingException extends RuntimeException {

    private final ErrorCode errorCode;

    public EfilingException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.name();
    }
}
