package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageResponse;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;

import java.math.BigDecimal;

public class EfilingSubmissionServiceDemoImpl implements EfilingSubmissionService {

    @Override
    public SubmitPackageResponse submitFilingPackage(AccountDetails accountDetails, FilingPackage efilingPackage, EfilingPaymentService payment) {
        return SubmitPackageResponse.builder().transactionId(BigDecimal.ONE).packageLink("http://demo").create();
    }
}
