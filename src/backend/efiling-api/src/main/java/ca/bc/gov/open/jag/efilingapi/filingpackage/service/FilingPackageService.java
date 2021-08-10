package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FilingPackageService {

    Optional<FilingPackage> getCSOFilingPackage(String universalId, BigDecimal packageNumber);

    Optional<List<FilingPackage>> getFilingPackages(String universalId, String parentApplication);

    Optional<Resource> getReport(ReportRequest reportRequest);

    Optional<SubmittedDocument> getSubmittedDocument(String universalId, BigDecimal packageNumber, BigDecimal documentIdentifier);

    void deleteSubmittedDocument(String universalId, BigDecimal packageNumber, String documentIdentifier);

    Optional<SubmittedDocument> getRushDocument(String universalId, BigDecimal packageNumber, String documentIdentifier);

}
