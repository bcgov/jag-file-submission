package ca.bc.gov.open.jag.efilingcommons.submission.service;

import ca.bc.gov.open.jag.efilingcommons.submission.models.Submission;

import java.util.Optional;
import java.util.UUID;

/**
 * A service to manage persistence of submissions objects
 */
public interface SubmissionStore {

    Optional<Submission> put(Submission submission);

    Optional<Submission> get(UUID submissionId, UUID owner);

}
