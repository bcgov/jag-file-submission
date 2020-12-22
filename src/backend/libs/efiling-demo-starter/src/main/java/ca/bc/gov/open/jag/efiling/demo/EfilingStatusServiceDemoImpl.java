package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;

import java.util.Optional;

public class EfilingStatusServiceDemoImpl implements EfilingStatusService {

    @Override
    public Optional<FilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {
        return Optional.empty();
    }
}
