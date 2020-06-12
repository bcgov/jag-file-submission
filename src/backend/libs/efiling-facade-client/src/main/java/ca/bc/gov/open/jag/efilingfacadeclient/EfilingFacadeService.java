package ca.bc.gov.open.jag.efilingfacadeclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;

import java.math.BigDecimal;

public interface EfilingFacadeService {

    public SubmitFilingResponse submitFiling(FilingPackage filingPackage);
}
