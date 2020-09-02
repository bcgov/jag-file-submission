package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.model.SubmitPackageResponse;

public interface EfilingSubmissionService {

    SubmitPackageResponse submitFilingPackage(
            SubmitPackageRequest submitPackageRequest,
            EfilingPaymentService payment);

}
