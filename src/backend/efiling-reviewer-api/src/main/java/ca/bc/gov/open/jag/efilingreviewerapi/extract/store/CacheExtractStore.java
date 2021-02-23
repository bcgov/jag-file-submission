package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import org.springframework.cache.annotation.CachePut;

import java.math.BigDecimal;
import java.util.Optional;

public class CacheExtractStore implements ExtractStore {

    @Override
    @CachePut(cacheNames = "extractRequest", key = "{ #id }", cacheManager = "extractRequestCacheManager")
    public Optional<ExtractRequest> put(BigDecimal id, ExtractRequest documentExtractRequest) {
        return Optional.of(documentExtractRequest);
    }

}
