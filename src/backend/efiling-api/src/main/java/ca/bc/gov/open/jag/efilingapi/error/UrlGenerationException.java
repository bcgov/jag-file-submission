package ca.bc.gov.open.jag.efilingapi.error;

public class UrlGenerationException extends EfilingException {
    public UrlGenerationException(String message) {
        super(message, ErrorCode.URL_GENERATION_FAILURE);
    }
}
