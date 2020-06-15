package ca.bc.gov.open.jagefilingapi.submission.service;

import ca.bc.gov.open.jagefilingapi.submission.models.Submission;

import java.util.Optional;
import java.util.UUID;

/**
 * A service to manage persistence of submissions objects
 */
public interface SubmissionService {

    Optional<Submission> put(UUID key, Submission submission);

    Optional<Submission> getByKey(UUID key);

}
