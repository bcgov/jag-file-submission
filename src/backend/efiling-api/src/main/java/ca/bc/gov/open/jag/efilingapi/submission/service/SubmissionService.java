package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageResponse;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;

import java.math.BigDecimal;
import java.util.UUID;

public interface SubmissionService {

    Submission generateFromRequest(UUID transactionId, UUID submissionId, GenerateUrlRequest generateUrlRequest);

    SubmitFilingPackageResponse submitFilingPackage(UUID authUserId, UUID submissionId, SubmitFilingPackageRequest submitFilingPackageRequest);

}
