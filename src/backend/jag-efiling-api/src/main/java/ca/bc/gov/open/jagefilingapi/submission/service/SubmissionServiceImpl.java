package ca.bc.gov.open.jagefilingapi.submission.service;

import ca.bc.gov.open.jagefilingapi.cache.StorageService;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;

import java.util.Optional;

/**
 * Implementation of SubmissionService using Submission StorageService
 */
public class SubmissionServiceImpl implements SubmissionService {


    private final StorageService<Submission> submissionStorageService;

    public SubmissionServiceImpl(StorageService<Submission> submissionStorageService) {
        this.submissionStorageService = submissionStorageService;
    }

    public String put(Submission submission) {
        return this.submissionStorageService.put(submission);
    }

    public Optional<Submission> getByKey(String key) {
        Submission result = this.submissionStorageService.getByKey(key, Submission.class);

        if (result == null) return Optional.empty();

        return Optional.of(result);

    }

}
