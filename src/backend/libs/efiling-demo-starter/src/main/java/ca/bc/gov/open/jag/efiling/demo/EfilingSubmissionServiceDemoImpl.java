package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;

import java.math.BigDecimal;

public class EfilingSubmissionServiceDemoImpl implements EfilingSubmissionService {

    @Override
    public BigDecimal submitFilingPackage(AccountDetails accountDetails, FilingPackage efilingPackage, EfilingFilingPackage filingPackage, boolean isRushedProcessing, EfilingPaymentService efilingPaymentService) {
        return BigDecimal.ONE;
    }

}
