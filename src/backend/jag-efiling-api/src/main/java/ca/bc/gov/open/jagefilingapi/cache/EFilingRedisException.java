package ca.bc.gov.open.jagefilingapi.cache;

public class FlaRedisException extends RuntimeException {

    public FlaRedisException(String message) {
        super(message);
    }


    public FlaRedisException(String message, Throwable cause) {
        super(message, cause);
    }
}
