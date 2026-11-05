package ca.bc.gov.open.jag.efilingcommons.submission;

import ca.bc.gov.open.jag.efilingcommons.model.RushDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EfilingReviewService {

    Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest);

    List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest);

    Optional<byte[]> getReport(ReportRequest reportRequest);

    Optional<byte[]> getSubmittedDocument(BigDecimal documentIdentifier);

    void deleteSubmittedDocument(DeleteSubmissionDocumentRequest deleteSubmissionDocumentRequest);

    /**
     * Call cso to get the byte array
     * @param rushDocumentRequest requested document identifiers
     * @return optional byte array
     */
    Optional<byte[]> getRushDocument(RushDocumentRequest rushDocumentRequest);

}
