package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;


public interface EfilingSubmissionService {

    public SubmitFilingResponse submitFiling(FilingPackage filingPackage);
}
