package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFeeRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;

import java.util.List;

public interface EfilingLookupService {

    ServiceFees getServiceFee(SubmissionFeeRequest submissionFeeRequest);

    List<String> getValidPartyRoles(String courtLevel, String courtClass, String documentTypes, String division);

    /**
     * Get countries code table lookup
     * @return a list of country code/description
     */
    List<LookupItem> getCountries();

}
