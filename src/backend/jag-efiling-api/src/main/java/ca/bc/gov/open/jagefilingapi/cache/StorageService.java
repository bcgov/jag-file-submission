package ca.bc.gov.open.jagefilingapi.cache;

public interface StorageService<T> {

    String put(T content);

    T getByKey(String key);

    void deleteByKey(String key);
}
