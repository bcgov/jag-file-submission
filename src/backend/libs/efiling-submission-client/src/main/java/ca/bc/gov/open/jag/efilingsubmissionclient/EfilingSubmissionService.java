package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.ag.csows.filing.FilingPackage;

import java.math.BigDecimal;


public interface EfilingSubmissionService {

    public BigDecimal submitFiling(FilingPackage filingPackage);
}
