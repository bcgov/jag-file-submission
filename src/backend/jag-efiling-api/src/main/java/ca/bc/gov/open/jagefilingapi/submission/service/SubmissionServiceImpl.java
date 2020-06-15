package ca.bc.gov.open.jagefilingapi.submission.service;

import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of SubmissionService using Submission StorageService
 */
public class SubmissionServiceImpl implements SubmissionService {


    @Override
    @CachePut(cacheNames = "submission", key = "#key")
    public Optional<Submission> put(UUID key, Submission submission) {
        return Optional.of(submission);
    }

    @Override
    @Cacheable(cacheNames = "submission", key = "#key")
    public Optional<Submission> getByKey(UUID key) {
        return Optional.empty();
    }

}
