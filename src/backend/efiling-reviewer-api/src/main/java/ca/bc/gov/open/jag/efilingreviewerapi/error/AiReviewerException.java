package ca.bc.gov.open.jag.efilingreviewerapi.error;

public class AiReviewerException extends RuntimeException {

    private ErrorCode errorCode;

    public AiReviewerException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AiReviewerException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.name();
    }
}
