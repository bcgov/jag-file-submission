package ca.bc.gov.open.jag.efilingcommons.submission.service;


import ca.bc.gov.open.jag.efilingcommons.model.api.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingcommons.model.api.SubmitFilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.model.api.SubmitFilingPackageResponse;
import ca.bc.gov.open.jag.efilingcommons.submission.models.Submission;

import java.util.UUID;

public interface SubmissionService {

    Submission generateFromRequest(UUID authUserId, UUID submissionId, GenerateUrlRequest generateUrlRequest);

    SubmitFilingPackageResponse submitFilingPackage(UUID authUserId, UUID submissionId, SubmitFilingPackageRequest submitFilingPackageRequest);

}
