package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of SubmissionStore using Submission StorageService
 */
public class SubmissionStoreImpl implements SubmissionStore {


    @Override
    @CachePut(cacheNames = "submission", key = "{ #submission.id, #submission.owner }", cacheManager = "submissionCacheManager")
    public Optional<Submission> put(Submission submission) {
        return Optional.of(submission);
    }

    @Override
    @Cacheable(cacheNames = "submission", key = "{ #submissionId, #owner }", cacheManager = "submissionCacheManager", unless="#result == null")
    public Optional<Submission> get(UUID submissionId, UUID owner) {
        return Optional.empty();
    }

}
