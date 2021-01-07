package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageResponse;

public interface EfilingSubmissionService {

    SubmitPackageResponse submitFilingPackage(
            AccountDetails accountDetails,
            FilingPackage efilingPackage,
            EfilingPaymentService payment);
}
