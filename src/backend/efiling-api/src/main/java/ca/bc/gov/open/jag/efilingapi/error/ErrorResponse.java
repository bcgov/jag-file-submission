package ca.bc.gov.open.jag.efilingapi.error;

public enum ErrorResponse {

    ACCOUNTEXCEPTION("Client has multiple CSO profiles."),
    DOCUMENT_TYPE_ERROR("Error while retrieving documents"),
    PAYMENT_FAILURE("Error while making payment"),
    SUBMISSION_FAILURE("Error while submitting filing package"),
    DOCUMENT_STORAGE_FAILURE("An unknown error happened while storing documents."),
    CREATE_ACCOUNT_EXCEPTION("Error Creating CSO account."),
    UPDATE_CLIENT_EXCEPTION("Error Updating CSO client account."),
    CACHE_ERROR("Cache related error."),
    URL_GENERATION_FAILURE("failed to generate bambora card update url."),
    DELETE_DOCUMENT_ERROR("Document deletion failed");

    private final String errorMessage;

    ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.toString();
    }

    public String getErrorMessage() { return errorMessage; }
}
