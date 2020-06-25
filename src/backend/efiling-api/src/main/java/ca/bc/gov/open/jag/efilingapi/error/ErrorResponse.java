package ca.bc.gov.open.jag.efilingapi.error;

public enum ErrorResponse {
    INVALIDROLE("INVROLE", "User does not have a valid role for this request."),
    ACCOUNTEXCEPTION("MLTACCNT", "Client has multiple CSO profiles"),
    GETPROFILESEXCEPTION("CSOERROR", "Calling CSO accountFacade.getProfiles caused an exception.");

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
