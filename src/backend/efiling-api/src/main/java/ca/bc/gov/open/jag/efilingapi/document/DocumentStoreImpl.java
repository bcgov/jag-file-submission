package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
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
    @CachePut(cacheNames = "document", key = "{ #submissionKey.universalId, #submissionKey.submissionId, #submissionKey.transactionId, #fileName }", cacheManager = "documentCacheManager")
    public byte[] put(SubmissionKey submissionKey, String fileName, byte[] content) {
        return content;
    }

    @Override
    @Cacheable(cacheNames = "document", key = "{ #submissionKey.universalId, #submissionKey.submissionId, #submissionKey.transactionId, #fileName }", cacheManager = "documentCacheManager", unless = "#result == null")
    public byte[] get(SubmissionKey submissionKey, String fileName) {
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "document", key = "{ #submissionKey.universalId, #submissionKey.submissionId, #submissionKey.transactionId, #fileName }", cacheManager = "documentCacheManager")
    public void evict(SubmissionKey submissionKey, String fileName) {
        //This implements Redis delete no code required
    }

    @Override
    @Cacheable(cacheNames = "documentDetails", cacheManager = "documentDetailsCacheManager", unless = "#result == null")
    public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
        return this.efilingDocumentService.getDocumentTypeDetails(courtLevel, courtClass, documentType);
    }

    @Override
    public List<DocumentType> getDocumentTypes(String courtLevel, String courtClass) {
        return this.efilingDocumentService.getDocumentTypes(courtLevel, courtClass);
    }
}
