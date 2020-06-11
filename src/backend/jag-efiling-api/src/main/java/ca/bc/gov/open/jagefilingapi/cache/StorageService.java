package ca.bc.gov.open.jagefilingapi.cache;

public interface StorageService<T> {

    String put(T content);

    T getByKey(String key, Class<T> clazz);

    void deleteByKey(String key);

}
