package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.api.model.ActionRequiredDetails;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.ParentAppDetails;
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

    /**
     * Get the required details for a package that requires action
     * @param universalId authorized users universal id
     * @param packageNumber requested package identifier
     * @return the action details for given user and package
     */
    Optional<ActionRequiredDetails> getActionRequiredDetails(String universalId, BigDecimal packageNumber);

    /**
     * Get the file related to the record
     * @param universalId authorized users universal id
     * @param packageNumber  requested package identifier
     * @param fileName required fileName
     * @return the byte array and meta associated with the document
     */
    Optional<SubmittedDocument> getRushDocument(String universalId, BigDecimal packageNumber, String fileName);

    /**
     * Get the parent app features for a package
     * @param universalId authorized users universal id
     * @param packageNumber  requested package identifier
     * @return feature for the packages parent application
     */
    Optional<ParentAppDetails> getParentDetails(String universalId, BigDecimal packageNumber);

}
