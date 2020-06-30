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
    @CachePut(cacheNames = "submission", key = "#submission.id", cacheManager = "submissionCacheManager")
    public Optional<Submission> put(Submission submission) {
        return Optional.of(submission);
    }

    @Override
    @Cacheable(cacheNames = "submission", key = "#key", cacheManager = "submissionCacheManager", unless="#result == null")
    public Optional<Submission> getByKey(UUID key) {
        return Optional.empty();
    }

}
