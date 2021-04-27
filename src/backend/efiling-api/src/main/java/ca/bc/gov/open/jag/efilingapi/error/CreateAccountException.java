package ca.bc.gov.open.jag.efilingapi.error;

public class CreateAccountException extends EfilingException {
    public CreateAccountException(String message) {
        super(message, ErrorCode.CREATE_ACCOUNT_EXCEPTION);
    }
}
