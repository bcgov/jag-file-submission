package ca.bc.gov.open.jag.efilingapi.error;

public class InvalidRoleException extends EfilingException {
    public InvalidRoleException(String message) {
        super(message, ErrorCode.INVALIDROLE);
    }
}
