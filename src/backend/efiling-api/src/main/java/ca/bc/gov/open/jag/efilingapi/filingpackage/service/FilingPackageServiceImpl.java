package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<FilingPackageRequest> request = buildFilingPackageRequest(universalId, packageNumber, null);

        if (!request.isPresent()) return Optional.empty();

        Optional<ReviewFilingPackage> filingPackage = efilingReviewService.findStatusByPackage(request.get());

        if (!filingPackage.isPresent()) return Optional.empty();

        return filingPackage.map(filingPackageMapper::toResponseFilingPackage);

    }

    @Override
    public Optional<List<FilingPackage>> getFilingPackages(String universalId, String parentApplication) {

        Optional<FilingPackageRequest> request = buildFilingPackageRequest(universalId, null, parentApplication);

        if (!request.isPresent()) return Optional.empty();

        List<ReviewFilingPackage> result = efilingReviewService.findStatusByClient(request.get());

        if (result == null || result.isEmpty()) return Optional.empty();

        return Optional.of(result.stream()
                .map(filingPackageMapper::toResponseFilingPackage)
                .collect(Collectors.toList()));

    }

    @Override
    public Optional<Resource> getReport(ReportRequest reportRequest) {

        Optional<byte[]> result = efilingReviewService.getReport(reportRequest);

        if (!result.isPresent()) return Optional.empty();

        Resource resource = new ByteArrayResource(result.get()) {
            @Override
            public String getFilename() {
                return reportRequest.getFileName();
            }
        };

        return Optional.of(resource);

    }

    @Override
    public Optional<SubmittedDocument> getSubmittedDocument(String universalId, BigDecimal packageNumber, BigDecimal documentIdentifier) {

        Optional<FilingPackageRequest> request = buildFilingPackageRequest(universalId, packageNumber, null);

        if (!request.isPresent()) return Optional.empty();

        Optional<ReviewFilingPackage> filingPackage = efilingReviewService.findStatusByPackage(request.get());

        if (!filingPackage.isPresent()) return Optional.empty();

        Optional<ReviewDocument> reviewDocument = filingPackage.get().getDocuments().stream().filter(document -> document.getDocumentId().equals(documentIdentifier.toString())).findFirst();

        if (!reviewDocument.isPresent()) return Optional.empty();

        Optional<byte[]> document = efilingReviewService.getSubmittedDocument(documentIdentifier);

        return document.map(bytes -> SubmittedDocument.builder()
                .name(reviewDocument.get().getFileName())
                .data(new ByteArrayResource(bytes))
                .create());

    }

    @Override
    public void deleteSubmittedDocument(String universalId, BigDecimal packageNumber, String documentIdentifier) {

        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if (accountDetails.getClientId() == null) throw new EfilingAccountServiceException("Account not found");

        efilingReviewService.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(accountDetails.getClientId(), packageNumber, null, documentIdentifier));

    }

    private Optional<FilingPackageRequest> buildFilingPackageRequest(String universalId, BigDecimal packageNumber, String parentApplication) {

        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if (accountDetails.getClientId() == null) return Optional.empty();

        return Optional.of(new FilingPackageRequest(accountDetails.getClientId(), packageNumber, parentApplication));

    }

}
