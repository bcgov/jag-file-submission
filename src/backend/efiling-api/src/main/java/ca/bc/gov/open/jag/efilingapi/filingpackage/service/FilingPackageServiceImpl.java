package ca.bc.gov.open.jag.efilingapi.filingpackage.service;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.ActionRequiredDetails;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.ParentAppDetails;
import ca.bc.gov.open.jag.efilingapi.error.NoRegistryNoticeException;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.ActionRequiredDetailsMapper;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import ca.bc.gov.open.jag.efilingapi.filingpackage.properties.ParentAppProperties;
import ca.bc.gov.open.jag.efilingapi.filingpackage.properties.ParentProperties;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.RushDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.RushDocument;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilingPackageServiceImpl implements FilingPackageService {

    private final EfilingReviewService efilingReviewService;

    private final EfilingDocumentService efilingDocumentService;

    private final AccountService accountService;

    private final FilingPackageMapper filingPackageMapper;

    private final ActionRequiredDetailsMapper actionRequiredDetailsMapper;

    private final ParentProperties parentProperties;

    public FilingPackageServiceImpl(EfilingReviewService efilingReviewService, EfilingDocumentService efilingDocumentService, AccountService accountService, FilingPackageMapper filingPackageMapper, ActionRequiredDetailsMapper actionRequiredDetailsMapper, ParentProperties parentProperties) {
        this.efilingReviewService = efilingReviewService;
        this.efilingDocumentService = efilingDocumentService;
        this.accountService = accountService;
        this.filingPackageMapper = filingPackageMapper;
        this.actionRequiredDetailsMapper = actionRequiredDetailsMapper;
        this.parentProperties = parentProperties;
    }


    @Override
    public Optional<FilingPackage> getCSOFilingPackage(String universalId, BigDecimal packageNumber) {

        Optional<FilingPackageRequest> request = buildFilingPackageRequest(universalId, packageNumber, null);

        if (!request.isPresent()) return Optional.empty();

        Optional<ReviewFilingPackage> filingPackage = efilingReviewService.findStatusByPackage(request.get());

        if (!filingPackage.isPresent()) return Optional.empty();

        filingPackage.get().getDocuments().forEach(
                reviewDocument -> reviewDocument.setRushRequired(efilingDocumentService.getDocumentTypeDetails(filingPackage.get().getCourt().getLevel(),filingPackage.get().getCourt().getCourtClass(), reviewDocument.getDocumentTypeCd(), Keys.DEFAULT_DIVISION).isRushRequired())
        );

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

        Optional<ReviewFilingPackage> filingPackage = getFilingPackage(reportRequest.getUniversalId(), reportRequest.getPackageId());

        if (!filingPackage.isPresent()) return Optional.empty();

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

        Optional<ReviewFilingPackage> filingPackage = getFilingPackage(universalId, packageNumber);

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

    @Override
    public Optional<ActionRequiredDetails> getActionRequiredDetails(String universalId, BigDecimal packageNumber) {

        Optional<FilingPackageRequest> request = buildFilingPackageRequest(universalId, packageNumber, null);

        if (!request.isPresent()) return Optional.empty();

        Optional<ReviewFilingPackage> filingPackage = efilingReviewService.findStatusByPackage(request.get());

        if (!filingPackage.isPresent()) return Optional.empty();

        if (Boolean.FALSE.equals(filingPackage.get().getHasRegistryNotice())) throw new NoRegistryNoticeException("This package does not have a registry notice");

        //For phase one of enhancements rejected document are the only ones included.
        return Optional.of(actionRequiredDetailsMapper.toActionRequiredDetails(filingPackage.get(),
                request.get().getClientId(),
                filingPackage.get().getDocuments().stream()
                    .filter(reviewDocument -> reviewDocument.getStatusCode().equalsIgnoreCase(Keys.REJECTED_DOCUMENT_CODE))
                    .map(actionRequiredDetailsMapper::toActionDocument)
                    .collect(Collectors.toList())));

    }

    @Override
    public Optional<SubmittedDocument> getRushDocument(String universalId, BigDecimal packageNumber, String fileName) {

        Optional<ReviewFilingPackage> filingPackage = getFilingPackage(universalId, packageNumber);

        if (!filingPackage.isPresent()) return Optional.empty();

        Optional<RushDocument> reviewDocument = filingPackage.get().getRushOrder().getSupportDocs().stream().filter(document -> document.getClientFileNm().equals(fileName)).findFirst();

        if (!reviewDocument.isPresent()) return Optional.empty();

        Optional<byte[]> document = efilingReviewService.getRushDocument(RushDocumentRequest.builder()
                .procReqId(reviewDocument.get().getProcessRequestId())
                .docSeqNo(reviewDocument.get().getProcessSupportDocSeqNo())
                .procItemSeqNo(reviewDocument.get().getProcessItemSeqNo())
                .create());

        return document.map(bytes -> SubmittedDocument.builder()
                .name(reviewDocument.get().getClientFileNm())
                .data(new ByteArrayResource(bytes))
                .create());

    }

    @Override
    public Optional<ParentAppDetails> getParentDetails(String universalId, BigDecimal packageNumber) {

        Optional<ReviewFilingPackage> filingPackage = getFilingPackage(universalId, packageNumber);

        if (!filingPackage.isPresent()) return Optional.empty();

        Optional<ParentAppProperties> parentAppProperties = parentProperties.getParents().stream()
                .filter(parentApp -> parentApp.getApplication().equalsIgnoreCase(filingPackage.get().getApplicationCode()))
                .findFirst();

        if (!parentAppProperties.isPresent()) return Optional.empty();

        ParentAppDetails parentAppDetails = new ParentAppDetails();

        parentAppDetails.setRejectedDocumentFeature(parentAppProperties.get().getRejectedDocuments());
        parentAppDetails.setReturnUrl(parentAppProperties.get().getReturnUrl());

        return Optional.of(parentAppDetails);

    }

    private Optional<FilingPackageRequest> buildFilingPackageRequest(String universalId, BigDecimal packageNumber, String parentApplication) {

        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if (accountDetails.getClientId() == null) return Optional.empty();

        return Optional.of(new FilingPackageRequest(accountDetails.getClientId(), accountDetails.getAccountId(), packageNumber, parentApplication));

    }

    private Optional<ReviewFilingPackage> getFilingPackage(String universalId, BigDecimal packageNumber) {
        Optional<FilingPackageRequest> request = buildFilingPackageRequest(universalId, packageNumber, null);

        if (!request.isPresent()) return Optional.empty();

        return efilingReviewService.findStatusByPackage(request.get());

    }

}
