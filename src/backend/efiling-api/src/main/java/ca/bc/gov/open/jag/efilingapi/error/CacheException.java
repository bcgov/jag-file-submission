package ca.bc.gov.open.jag.efilingapi.error;

public class CacheException extends EfilingException {
    public CacheException(String message) {
        super(message, ErrorCode.CACHE_ERROR);
    }
}
