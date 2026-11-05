package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.UpdateDocumentRequest;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingapi.submission.models.GetValidPartyRoleRequest;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;

import java.util.List;

public interface SubmissionService {

    Submission generateFromRequest(String applicationCode, SubmissionKey submissionKey, GenerateUrlRequest generateUrlRequest);

    SubmitResponse createSubmission(Submission submission, AccountDetails accountDetails, Boolean isEarlyAdopter);

    Submission updateDocuments(Submission submission, UpdateDocumentRequest updateDocumentRequest, SubmissionKey submissionKey);

    Boolean isRushRequired(String documentType, String courtLevel, String courtClass);

    List<String> getValidPartyRoles(GetValidPartyRoleRequest getValidPartyRoleRequest);

}
