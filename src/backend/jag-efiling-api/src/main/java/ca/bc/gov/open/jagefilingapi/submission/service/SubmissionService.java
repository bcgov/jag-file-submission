package ca.bc.gov.open.jagefilingapi.submission.service;

import ca.bc.gov.open.jagefilingapi.submission.models.Submission;

import java.util.Optional;

/**
 * A service to manage persistence of submissions objects
 */
public interface SubmissionService {

    String put(Submission submission);

    Optional<Submission> getByKey(String key);

}
