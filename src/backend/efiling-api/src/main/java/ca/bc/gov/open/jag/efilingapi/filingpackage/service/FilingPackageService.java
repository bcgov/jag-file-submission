package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface FilingPackageService {

    Optional<FilingPackage> getCSOFilingPackage(UUID universalId, BigDecimal packageNumber);
}
