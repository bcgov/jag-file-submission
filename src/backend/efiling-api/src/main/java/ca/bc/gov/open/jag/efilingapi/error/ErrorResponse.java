package ca.bc.gov.open.jag.efilingapi.error;

public enum ErrorResponse {
    INVALIDROLE("User does not have a valid role for this request."),
    ACCOUNTEXCEPTION("Client has multiple CSO profiles."),
    GETPROFILESEXCEPTION("Calling CSO accountFacade.getProfiles caused an exception."),
    CREATE_ACCOUNT_EXCEPTION("Error Creating CSO account."),
    CACHE_ERROR("Cache related error.");

    private final String errorMessage;

    ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.toString();
    }

    public String getErrorMessage() { return errorMessage; }
}
