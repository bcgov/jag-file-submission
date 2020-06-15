package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;

import java.math.BigDecimal;

public class MockCSOSubmissionServiceImpl implements EfilingSubmissionService {

    @Override
    public SubmitFilingResponse submitFiling(FilingPackage filingPackage) {

        SubmitFilingResponse submitFilingResponse = new SubmitFilingResponse();
        submitFilingResponse.setReturn(new BigDecimal(1));
        return submitFilingResponse;
    }
}
