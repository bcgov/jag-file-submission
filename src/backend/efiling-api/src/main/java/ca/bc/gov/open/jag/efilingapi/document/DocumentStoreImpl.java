package ca.bc.gov.open.jag.efilingapi.document;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class DocumentStoreImpl implements DocumentStore {

    @Override
    @CachePut(cacheNames = "document", key = "#compositeId", cacheManager = "documentCacheManager")
    public byte[] put(String compositeId, byte[] content) {
        return content;
    }

    @Override
    @Cacheable(cacheNames = "document", key = "#compositeId", cacheManager = "documentCacheManager", unless = "#result == null")
    public byte[] get(String compositeId) {
        return null;
    }

}
