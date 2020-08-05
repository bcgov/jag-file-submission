package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.CreateServiceResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentResponse;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;

import java.util.UUID;

public interface SubmissionService {

    Submission generateFromRequest(UUID transactionId, UUID submissionId, GenerateUrlRequest generateUrlRequest);

    CreateServiceResponse createSubmission(Submission submission);

    Submission updateDocuments(Submission submission, UpdateDocumentRequest updateDocumentRequest);

}
