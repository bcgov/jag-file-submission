package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;

public interface SubmissionService {

    Submission generateFromRequest(GenerateUrlRequest generateUrlRequest) throws NestedEjbException_Exception;

}
