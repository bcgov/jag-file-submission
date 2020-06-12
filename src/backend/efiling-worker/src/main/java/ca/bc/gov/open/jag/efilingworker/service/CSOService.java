package ca.bc.gov.open.jag.efilingworker.service;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;

public interface CSOService {
   SubmitFilingResponse submitFiling(FilingPackage filingPackage);
}
