package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;

import java.math.BigDecimal;
import java.util.Optional;

public interface FilingPackageService {

    Optional<FilingPackage> getCSOFilingPackage(String universalId, BigDecimal packageNumber);

    Optional<byte[]> getSubmissionSheet(BigDecimal packageNumber);

    Optional<byte[]> getSubmissionDocument(BigDecimal packageNumber, String documentIdentifier);
}
