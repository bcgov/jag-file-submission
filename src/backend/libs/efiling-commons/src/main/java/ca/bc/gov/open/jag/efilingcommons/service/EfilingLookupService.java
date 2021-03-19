package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFeeRequest;

import java.util.List;

public interface EfilingLookupService {

    ServiceFees getServiceFee(SubmissionFeeRequest submissionFeeRequest);

    List<String> getValidPartyRoles(String courtLevel, String courtClass, String documentTypes);
}
