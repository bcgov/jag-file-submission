package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.PartyMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.GetValidPartyRoleRequest;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingapi.utils.FileUtils;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.Court;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubmissionServiceImpl implements SubmissionService {

    Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    private final SubmissionStore submissionStore;

    private final CacheProperties cacheProperties;

    private final SubmissionMapper submissionMapper;

    private final PartyMapper partyMapper;

    private final EfilingLookupService efilingLookupService;

    private final EfilingCourtService efilingCourtService;

    private final EfilingSubmissionService efilingSubmissionService;

    private final DocumentStore documentStore;

    private final PaymentAdapter paymentAdapter;

    private final SftpService sftpService;

    private final NavigationProperties navigationProperties;

    public SubmissionServiceImpl(
            SubmissionStore submissionStore,
            CacheProperties cacheProperties,
            SubmissionMapper submissionMapper,
            PartyMapper partyMapper,
            EfilingLookupService efilingLookupService,
            EfilingCourtService efilingCourtService,
            EfilingSubmissionService efilingSubmissionService,
            DocumentStore documentStore,
            PaymentAdapter paymentAdapter,
            SftpService sftpService, NavigationProperties navigationProperties) {
        this.submissionStore = submissionStore;
        this.cacheProperties = cacheProperties;
        this.submissionMapper = submissionMapper;
        this.partyMapper = partyMapper;
        this.efilingLookupService = efilingLookupService;
        this.efilingCourtService = efilingCourtService;
        this.efilingSubmissionService = efilingSubmissionService;
        this.documentStore = documentStore;
        this.paymentAdapter = paymentAdapter;
        this.sftpService = sftpService;
        this.navigationProperties = navigationProperties;
    }


    @Override
    public Submission generateFromRequest(String applicationCode, SubmissionKey submissionKey, GenerateUrlRequest generateUrlRequest) {

        Optional<Submission> cachedSubmission = submissionStore.put(
                submissionMapper.toSubmission(
                        submissionKey.getUniversalId(),
                        submissionKey.getSubmissionId(),
                        submissionKey.getTransactionId(),
                        generateUrlRequest,
                        toFilingPackage(applicationCode, generateUrlRequest, submissionKey),
                        getExpiryDate()));

        if (!cachedSubmission.isPresent()) throw new StoreException("exception while storing submission object");

        return cachedSubmission.get();

    }

    private boolean isRushedSubmission(GenerateUrlRequest generateUrlRequest) {

        for (InitialDocument initialDocument : generateUrlRequest.getFilingPackage().getDocuments()) {
            DocumentDetails documentDetails = documentStore.getDocumentDetails(generateUrlRequest.getFilingPackage().getCourt().getLevel(), generateUrlRequest.getFilingPackage().getCourt().getCourtClass(), initialDocument.getDocumentProperties().getType().getValue());
            if (documentDetails.isRushRequired()) return true;
        }
        return false;
    }

    @Override
    public SubmitResponse createSubmission(Submission submission, AccountDetails accountDetails, Boolean isEarlyAdopter) {

        uploadFiles(submission);

        SubmitResponse result = new SubmitResponse();

        SubmitPackageResponse submitPackageResponse = efilingSubmissionService
                .submitFilingPackage(
                        accountDetails,
                        submission.getFilingPackage(),
                        paymentAdapter::makePayment);

        if(isEarlyAdopter) {
            result.setPackageRef(
                    Base64.getEncoder().encodeToString(
                            MessageFormat.format("{0}/packagereview/{1}",
                                    navigationProperties.getBaseUrl(),
                                    submitPackageResponse.getTransactionId().toPlainString()).getBytes()));
        } else {
            result.setPackageRef(Base64.getEncoder().encodeToString(submitPackageResponse.getPackageLink().getBytes()));
        }

        logger.info("successfully submitted efiling package with cso id [{}]", submitPackageResponse.getTransactionId());

        return result;

    }

    @Override
    public Submission updateDocuments(Submission submission, UpdateDocumentRequest updateDocumentRequest, SubmissionKey submissionKey) {

        updateDocumentRequest.getDocuments().forEach(documentProperties -> {
            submission.getFilingPackage().addDocument(toDocument(
                    submission.getFilingPackage().getCourt().getLevel(),
                    submission.getFilingPackage().getCourt().getCourtClass(),
                    documentProperties, submissionKey));
        });

        submissionStore.put(submission);

        return submission;
    }

    @Override
    public List<String> getValidPartyRoles(GetValidPartyRoleRequest getValidPartyRoleRequest) {
        return efilingLookupService.getValidPartyRoles(
                getValidPartyRoleRequest.getCourtLevel(),
                getValidPartyRoleRequest.getCourtClassification(),
                getValidPartyRoleRequest.getDocumentTypesAsString()
        );
    }

    private FilingPackage toFilingPackage(String applicationCode, GenerateUrlRequest request, SubmissionKey submissionKey) {

        return FilingPackage.builder()
                .court(populateCourtDetails(request.getFilingPackage().getCourt()))
                .submissionFeeAmount(getSubmissionFeeAmount())
                .documents(request.getFilingPackage()
                        .getDocuments()
                        .stream()
                        .map(documentProperties -> toDocument(
                                request.getFilingPackage().getCourt().getLevel(),
                                request.getFilingPackage().getCourt().getCourtClass(),
                                documentProperties, submissionKey))
                        .collect(Collectors.toList()))
                .parties(request.getFilingPackage()
                        .getParties()
                        .stream()
                        .map(partyMapper::toParty)
                        .collect(Collectors.toList()))
                .rushedSubmission(isRushedSubmission(request))
                .applicationCode(applicationCode)
                .create();

    }

    private Court populateCourtDetails(CourtBase courtBase) {

        CourtDetails courtDetails = efilingCourtService.getCourtDescription(courtBase.getLocation(), courtBase.getLevel(), courtBase.getCourtClass());

        return Court
                .builder()
                .location(courtBase.getLocation())
                .level(courtBase.getLevel())
                .courtClass(courtBase.getCourtClass())
                .division(courtBase.getDivision())
                .fileNumber(courtBase.getFileNumber())
                .agencyId(courtDetails.getCourtId())
                .locationDescription(courtDetails.getCourtDescription())
                .classDescription(courtDetails.getClassDescription())
                .levelDescription(courtDetails.getLevelDescription())
                .create();
    }

    private Document toDocument(String courtLevel, String courtClass,  InitialDocument initialDocument, SubmissionKey submissionKey) {

        DocumentDetails details = documentStore.getDocumentDetails(courtLevel, courtClass, initialDocument.getDocumentProperties().getType().getValue());

        return
                Document.builder()
                        .description(details.getDescription())
                        .statutoryFeeAmount(details.getStatutoryFeeAmount())
                        .type(initialDocument.getDocumentProperties().getType().getValue())
                        .name(initialDocument.getDocumentProperties().getName())
                        .serverFileName(MessageFormat.format("fh_{0}_{1}_{2}",submissionKey.getSubmissionId(), submissionKey.getTransactionId(), initialDocument.getDocumentProperties().getName()))
                        .mimeType(FileUtils.guessContentTypeFromName(initialDocument.getDocumentProperties().getName()))
                        .isAmendment(initialDocument.getIsAmendment())
                        .isSupremeCourtScheduling(initialDocument.getIsSupremeCourtScheduling())
                        .subType(details.getOrderDocument() ? SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD : SubmissionConstants.SUBMISSION_ODOC_DOCUMENT_SUB_TYPE_CD)
                        .data(initialDocument.getData())
                        .create();

    }

    private void uploadFiles(Submission submission) {
        submission.getFilingPackage().getDocuments().forEach(
                document ->
                        redisStoreToSftpStore(document, submission));

    }

    private void redisStoreToSftpStore(Document document, Submission submission) {

        SubmissionKey submissionKey = new SubmissionKey(submission.getUniversalId(), submission.getTransactionId(), submission.getId());

        sftpService.put(new ByteArrayInputStream(documentStore.get(submissionKey, document.getName())),
                document.getServerFileName());

        //Delete file from cache
        documentStore.evict(submissionKey, document.getName());

    }

    private BigDecimal getSubmissionFeeAmount() {
        // TODO: fix with the mapper ApplicationCode to ServiceTypeCode
        ServiceFees fee = efilingLookupService.getServiceFee(SubmissionConstants.SUBMISSION_FEE_TYPE);
        return fee == null ? BigDecimal.ZERO : fee.getFeeAmount();
    }

    private long getExpiryDate() {
        return System.currentTimeMillis() + cacheProperties.getRedis().getTimeToLive().toMillis();
    }

}
