package ca.bc.gov.open.jag.efilingapi.error;

public class UpdateClientException extends EfilingException {
    public UpdateClientException(String message) {
        super(message, ErrorCode.UPDATE_CLIENT_EXCEPTION);
    }
}
