package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingSubmissionTestService;

public class CsoEfilingSubmittionTestServiceImpl implements EfilingSubmissionTestService {

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public CsoEfilingSubmittionTestServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
    }

    @Override
    public void test() {
        System.out.println("test");
    }
}
