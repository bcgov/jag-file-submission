package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractResponse;
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
    @CachePut(cacheNames = "extractResponse", key = "{ #id }", cacheManager = "extractRequestCacheManager")
    public Optional<ExtractResponse> put(BigDecimal id, ExtractResponse documentExtractResponse) {
        return Optional.of(documentExtractResponse);
    }


}
