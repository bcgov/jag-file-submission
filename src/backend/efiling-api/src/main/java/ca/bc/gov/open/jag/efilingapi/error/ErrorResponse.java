package ca.bc.gov.open.jag.efilingapi.error;

public enum ErrorResponse {

    INVALIDROLE("User does not have a valid role for this request."),
    INVALIDUNIVERSAL("Invalid universal id."),
    ACCOUNTEXCEPTION("Client has multiple CSO profiles."),
    DOCUMENT_TYPE_ERROR("Error while retrieving documents"),
    PAYMENT_FAILURE("Error while making payment"),
    SUBMISSION_FAILURE("Error while submitting filing package"),
    DOCUMENT_REQUIRED("At least one document is required."),
    DOCUMENT_STORAGE_FAILURE("An unknown error happened while storing documents."),
    FILE_TYPE_ERROR("File is not a PDF"),
    CREATE_ACCOUNT_EXCEPTION("Error Creating CSO account."),
    UPDATE_CLIENT_EXCEPTION("Error Updating CSO client account."),
    CACHE_ERROR("Cache related error."),
    MISSING_UNIVERSAL_ID("universal-id claim missing in jwt token."),
    URL_GENERATION_FAILURE("failed to generate bambora card update url."),
    INVALID_INITIAL_SUBMISSION_PAYLOAD("Initial Submission payload invalid, find more in the details array."),
    COURT_LOCATION_ERROR("Error while retrieving court locations."),
    MISSING_APPLICATION_CODE("Missing application code claim. Contact administrator"),
    FILING_PACKAGE_NOT_FOUND("Requested filing package was not found."),
    SUBMISSION_SHEET_NOT_FOUND("Requested submission sheet was not found."),
    DOCUMENT_NOT_FOUND("Requested document was not found.");

    private final String errorMessage;

    ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.toString();
    }

    public String getErrorMessage() { return errorMessage; }
}
