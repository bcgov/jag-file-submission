package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

/**
 * Implementation of SubmissionStore using Submission StorageService
 */
public class SubmissionStoreImpl implements SubmissionStore {


    @Override
    @CachePut(cacheNames = "submission", key = "{ #p0.getUniversalId(), #p0.getSubmissionId(), #p0.getTransactionId() }", cacheManager = "submissionCacheManager")
    public Optional<Submission> put(Submission submission) {
        return Optional.of(submission);
    }

    @Override
    @Cacheable(cacheNames = "submission", key = "{ #p0.getUniversalId(), #p0.getSubmissionId(), #p0.getTransactionId() }", cacheManager = "submissionCacheManager", unless="#result == null")
    public Optional<Submission> get(SubmissionKey submissionKey) {
        return Optional.empty();
    }

    @Override
    @CacheEvict(cacheNames = "submission", key = "{ #p0.getUniversalId(), #p0.getSubmissionId(), #p0.getTransactionId() }", cacheManager = "submissionCacheManager")
    public void evict(SubmissionKey submissionKey) {
        //This implements Redis delete no code required
    }

}
