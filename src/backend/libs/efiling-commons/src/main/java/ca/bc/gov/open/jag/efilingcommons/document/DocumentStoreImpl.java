package ca.bc.gov.open.jag.efilingcommons.document;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingDocumentService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class DocumentStoreImpl implements DocumentStore {

    private final EfilingDocumentService efilingDocumentService;

    public DocumentStoreImpl(EfilingDocumentService efilingDocumentService) {
        this.efilingDocumentService = efilingDocumentService;
    }

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

    @Override
    @Cacheable(cacheNames = "documentDetails", cacheManager = "documentDetailsCacheManager", unless = "#result == null")
    public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
        return this.efilingDocumentService.getDocumentDetails(courtLevel, courtClass, documentType);
    }
}
