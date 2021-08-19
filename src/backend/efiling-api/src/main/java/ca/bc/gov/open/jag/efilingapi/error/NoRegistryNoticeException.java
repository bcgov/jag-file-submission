package ca.bc.gov.open.jag.efilingapi.error;

public class NoRegistryNoticeException extends EfilingException {
    public NoRegistryNoticeException(String message) {
        super(message, ErrorCode.MISSING_REGISTRY_NOTICE);
    }
}
