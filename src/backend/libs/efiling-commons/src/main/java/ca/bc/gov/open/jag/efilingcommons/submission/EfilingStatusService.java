package ca.bc.gov.open.jag.efilingcommons.submission;

import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;

import java.util.Optional;

public interface EfilingStatusService {

    Optional<FilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest);

}
