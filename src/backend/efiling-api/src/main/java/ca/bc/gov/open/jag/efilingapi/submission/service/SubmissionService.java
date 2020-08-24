package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentRequest;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;

import java.util.UUID;

public interface SubmissionService {

    Submission generateFromRequest(UUID transactionId, UUID submissionId, UUID universalId, GenerateUrlRequest generateUrlRequest);

    SubmitResponse createSubmission(Submission submission);

    Submission updateDocuments(Submission submission, UpdateDocumentRequest updateDocumentRequest);

}
