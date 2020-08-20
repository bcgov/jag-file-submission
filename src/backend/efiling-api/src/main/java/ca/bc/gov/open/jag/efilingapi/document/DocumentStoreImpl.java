package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

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
    @Cacheable(cacheNames = "document", key = "#compositeId", cacheManager = "documentCacheManager")
    public void evict(String compositeId) {
        //This implements Redis delete no code required
    }

    @Override
    @Cacheable(cacheNames = "documentDetails", cacheManager = "documentDetailsCacheManager", unless = "#result == null")
    public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
        return this.efilingDocumentService.getDocumentDetails(courtLevel, courtClass, documentType);
    }

    @Override
    public List<DocumentType> getDocumentTypes(String courtLevel, String courtClass) {
        return this.efilingDocumentService.getDocumentTypes(courtLevel, courtClass);
    }
}
