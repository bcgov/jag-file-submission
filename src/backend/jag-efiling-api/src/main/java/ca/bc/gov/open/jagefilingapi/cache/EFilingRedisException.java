package ca.bc.gov.open.jagefilingapi.cache;

public class EFilingRedisException extends RuntimeException {

    public EFilingRedisException(String message) {
        super(message);
    }


    public EFilingRedisException(String message, Throwable cause) {
        super(message, cause);
    }
}
