package ca.bc.gov.open.jag.efilingapi.filepackage.service;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;

import java.math.BigDecimal;
import java.util.UUID;

public interface FilePackageService {

    FilingPackage getCSOFilingPackage(UUID universalId, BigDecimal packageNumber);
}
