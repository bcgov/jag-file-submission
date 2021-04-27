package ca.bc.gov.open.jag.efilingapi.error;

public class AccountException extends EfilingException {
    public AccountException(String message) {
        super(message, ErrorCode.ACCOUNTEXCEPTION);
    }
}
