package ca.bc.gov.open.jag.efilingapi.error;

public class AccountException extends EfilingException {
	private static final long serialVersionUID = 1L;

	public AccountException(String message) {
		super(message, ErrorCode.ACCOUNTEXCEPTION);
	}
}
