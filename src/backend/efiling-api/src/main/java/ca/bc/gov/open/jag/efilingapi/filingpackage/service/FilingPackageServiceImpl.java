package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

public class FilingPackageServiceImpl implements FilingPackageService {

    private final EfilingReviewService efilingReviewService;

    private final AccountService accountService;

    private final FilingPackageMapper filingPackageMapper;

    public FilingPackageServiceImpl(EfilingReviewService efilingReviewService, AccountService accountService, FilingPackageMapper filingPackageMapper) {
        this.efilingReviewService = efilingReviewService;
        this.accountService = accountService;
        this.filingPackageMapper = filingPackageMapper;
    }


    @Override
    public Optional<FilingPackage> getCSOFilingPackage(String universalId, BigDecimal packageNumber) {

        Optional<ReviewFilingPackage> filingPackage = getFilingPackage(universalId, packageNumber);

        if (!filingPackage.isPresent()) return Optional.empty();

        return filingPackage.map(filingPackageMapper::toResponseFilingPackage);

    }

    @Override
    public Optional<Resource> getSubmissionSheet(BigDecimal packageNumber) {

        Optional<byte[]> result = efilingReviewService.getSubmissionSheet(packageNumber);

        return result.map(ByteArrayResource::new);

    }

    @Override
    public Optional<SubmittedDocument> getSubmittedDocument(String universalId, BigDecimal packageNumber, String documentIdentifier) {

        Optional<ReviewFilingPackage> filingPackage = getFilingPackage(universalId, packageNumber);

        if (!filingPackage.isPresent()) return Optional.empty();

        Optional<ReviewDocument> reviewDocument = filingPackage.get().getDocuments().stream().filter(document -> document.getDocumentId().equals(documentIdentifier)).findFirst();

        if (!reviewDocument.isPresent()) return Optional.empty();

        Optional<byte[]> document = efilingReviewService.getSubmittedDocument(packageNumber, documentIdentifier);

        return document.map(bytes -> SubmittedDocument.builder()
                .name(reviewDocument.get().getFileName())
                .data(new ByteArrayResource(bytes))
                .create());

    }

    @Override
    public void deleteSubmittedDocument(String universalId, BigDecimal packageNumber, String documentIdentifier) {

        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if (accountDetails.getClientId() == null) throw new NotFoundException();

        efilingReviewService.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(accountDetails.getClientId(), packageNumber, documentIdentifier));

    }

    private Optional<ReviewFilingPackage> getFilingPackage(String universalId, BigDecimal packageNumber) {
        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if (accountDetails.getClientId() == null) return Optional.empty();

        FilingPackageRequest request = new FilingPackageRequest(accountDetails.getClientId(), packageNumber);

        return efilingReviewService.findStatusByPackage(request);
        
    }

}
