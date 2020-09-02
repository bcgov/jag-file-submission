package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageResponse;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;

import java.math.BigDecimal;

public class EfilingSubmissionServiceDemoImpl implements EfilingSubmissionService {

    @Override
    public SubmitPackageResponse submitFilingPackage(SubmitPackageRequest submitPackageRequest, EfilingPaymentService payment) {
        return SubmitPackageResponse.builder().transactionId(BigDecimal.TEN).packageLink("http://demo").create();
    }
}
