package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.Optional;

public interface FilingPackageService {

    Optional<FilingPackage> getCSOFilingPackage(String universalId, BigDecimal packageNumber);

    Optional<Resource> getSubmissionSheet(BigDecimal packageNumber);

    Optional<SubmittedDocument> getSubmittedDocument(String universalId, BigDecimal packageNumber, String documentIdentifier);

    void deleteSubmittedDocument(String universalId, BigDecimal packageNumber, String documentIdentifier);

}
