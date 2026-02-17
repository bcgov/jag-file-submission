package ca.bc.gov.open.jag.efilingapi.error;

public class MissingIdentityProviderException extends EfilingException {
    public MissingIdentityProviderException(String message) {
        super(message, ErrorCode.MISSING_IDENTITY_PROVIDER);
    }
}
