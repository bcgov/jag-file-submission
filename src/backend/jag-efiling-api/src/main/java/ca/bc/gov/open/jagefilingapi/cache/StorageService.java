package ca.bc.gov.open.jagefilingapi.cache;

public interface StorageService {

    String put(byte[] content);

    byte[] get(String key);

    void delete(String key);
}
