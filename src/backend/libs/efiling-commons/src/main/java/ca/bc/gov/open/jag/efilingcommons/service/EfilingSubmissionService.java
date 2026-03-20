package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageResponse;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;

public interface EfilingSubmissionService {

    SubmitPackageResponse submitFilingPackage(
            AccountDetails accountDetails,
            FilingPackage efilingPackage,
            EfilingPaymentService payment);
}
