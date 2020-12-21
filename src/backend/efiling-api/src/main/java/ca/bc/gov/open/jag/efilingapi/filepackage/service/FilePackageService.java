package ca.bc.gov.open.jag.efilingapi.filepackage.service;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;

import java.math.BigDecimal;

public interface FilePackageService {

    FilingPackage getCSOFilingPackage(BigDecimal clientId, BigDecimal packageNumber);
}
