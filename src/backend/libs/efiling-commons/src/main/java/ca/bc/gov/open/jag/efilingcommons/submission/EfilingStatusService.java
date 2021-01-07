package ca.bc.gov.open.jag.efilingcommons.submission;

import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;

import java.util.Optional;

public interface EfilingStatusService {

    Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest);

}
