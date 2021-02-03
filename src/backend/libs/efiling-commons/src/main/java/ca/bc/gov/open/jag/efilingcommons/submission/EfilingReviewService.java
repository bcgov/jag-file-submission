package ca.bc.gov.open.jag.efilingcommons.submission;

import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EfilingReviewService {

    Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest);

    List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest);

    Optional<byte[]> getSubmissionSheet(BigDecimal packageNumber);

    Optional<byte[]> getSubmittedDocument(BigDecimal packageNumber,
                                          String documentIdentifier);

    boolean deleteSubmittedDocument(DeleteSubmissionDocumentRequest deleteSubmissionDocumentRequest);

}
