package ca.bc.gov.open.jagefilingapi.error;

public enum ErrorResponse {
    INVALIDROLE("INVROLE", "User does not hava a valid role for this request.");

    private final String errorCode;
    private final String errorMessage;

    ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public String getErrorMessage() { return errorMessage; }
}
