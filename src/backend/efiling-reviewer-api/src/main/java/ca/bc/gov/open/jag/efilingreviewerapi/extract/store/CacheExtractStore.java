package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.Optional;

public class CacheExtractStore implements ExtractStore {

    @Override
    @CachePut(cacheNames = "extractRequest", key = "{ #id }", cacheManager = "extractRequestCacheManager")
    public Optional<ExtractRequest> put(BigDecimal id, ExtractRequest documentExtractRequest) {
        return Optional.of(documentExtractRequest);
    }

    @Override
    @Cacheable(cacheNames = "extractRequest", key = "{ #id }", cacheManager = "extractRequestCacheManager")
    public Optional<ExtractRequest> get(BigDecimal id) {
        return Optional.empty();
    }

    @Override
    @CacheEvict(cacheNames = "extractRequest", key = "{ #id }", cacheManager = "extractRequestCacheManager")
    public void evict(BigDecimal id) {
        //No code required
    }

    @Override
    @CachePut(cacheNames = "extractResponse", key = "{ #id }", cacheManager = "extractResponseCacheManager")
    public Optional<ExtractResponse> put(BigDecimal id, ExtractResponse documentExtractResponse) {
        return Optional.of(documentExtractResponse);
    }

    @Override
    @Cacheable(cacheNames = "extractResponse", key = "{ #id }", cacheManager = "extractResponseCacheManager")
    public Optional<ExtractResponse> getResponse(BigDecimal id) {
        return Optional.empty();
    }

    @Override
    @CacheEvict(cacheNames = "extractResponse", key = "{ #id }", cacheManager = "extractResponseCacheManager")
    public void evictResponse(BigDecimal id) {
        //No code required
    }

}
