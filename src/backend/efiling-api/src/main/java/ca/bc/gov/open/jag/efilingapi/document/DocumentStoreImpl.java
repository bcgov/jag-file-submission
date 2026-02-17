package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
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
    @Cacheable(cacheNames = "documentDetails", cacheManager = "documentTypeDetailsCacheManager", unless = "#result == null")
    public DocumentTypeDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
        return this.efilingDocumentService.getDocumentTypeDetails(courtLevel, courtClass, documentType, Keys.DEFAULT_DIVISION);
    }

    @Override
    public List<DocumentTypeDetails> getDocumentTypes(String courtLevel, String courtClass) {
        return this.efilingDocumentService.getDocumentTypes(courtLevel, courtClass, Keys.DEFAULT_DIVISION);
    }

    @Override
    @CachePut(cacheNames = "rushDocument", key = "{ #submissionKey.universalId, #submissionKey.submissionId, #submissionKey.transactionId, #fileName }", cacheManager = "documentCacheManager")
    public byte[] putRushDocument(SubmissionKey submissionKey, String fileName, byte[] content) {
        return content;
    }

    @Override
    @Cacheable(cacheNames = "rushDocument", key = "{ #submissionKey.universalId, #submissionKey.submissionId, #submissionKey.transactionId, #fileName }", cacheManager = "documentCacheManager", unless = "#result == null")
    public byte[] getRushDocument(SubmissionKey submissionKey, String fileName) {
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "rushDocument", key = "{ #submissionKey.universalId, #submissionKey.submissionId, #submissionKey.transactionId, #fileName }", cacheManager = "documentCacheManager")
    public void evictRushDocument(SubmissionKey submissionKey, String fileName) {
        //This implements Redis delete no code required
    }


}
