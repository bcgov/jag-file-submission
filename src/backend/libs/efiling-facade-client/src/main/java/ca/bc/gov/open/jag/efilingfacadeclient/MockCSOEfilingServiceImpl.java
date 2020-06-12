package ca.bc.gov.open.jag.efilingfacadeclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;

import java.math.BigDecimal;

public class MockCSOEfilingServiceImpl implements EfilingFacadeService {

    @Override
    public SubmitFilingResponse submitFiling(FilingPackage filingPackage) {

        SubmitFilingResponse submitFilingResponse = new SubmitFilingResponse();
        submitFilingResponse.setReturn(new BigDecimal(1));
        return submitFilingResponse;
    }
}
