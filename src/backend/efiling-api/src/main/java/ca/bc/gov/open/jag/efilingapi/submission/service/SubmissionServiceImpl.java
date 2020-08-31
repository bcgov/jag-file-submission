package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.EfilingFilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Court;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingapi.utils.FileUtils;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.sftp.starter.SftpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class SubmissionServiceImpl implements SubmissionService {

    Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    private final SubmissionStore submissionStore;

    private final CacheProperties cacheProperties;

    private final SubmissionMapper submissionMapper;

    private final EfilingFilingPackageMapper efilingFilingPackageMapper;

    private final EfilingLookupService efilingLookupService;

    private final EfilingCourtService efilingCourtService;

    private final EfilingSubmissionService efilingSubmissionService;

    private final DocumentStore documentStore;

    private final BamboraPaymentAdapter bamboraPaymentAdapter;

    private final SftpService sftpService;

    public SubmissionServiceImpl(
            SubmissionStore submissionStore,
            CacheProperties cacheProperties,
            SubmissionMapper submissionMapper,
            EfilingFilingPackageMapper efilingFilingPackageMapper,
            EfilingLookupService efilingLookupService,
            EfilingCourtService efilingCourtService,
            EfilingSubmissionService efilingSubmissionService,
            DocumentStore documentStore,
            BamboraPaymentAdapter bamboraPaymentAdapter,
            SftpService sftpService) {
        this.submissionStore = submissionStore;
        this.cacheProperties = cacheProperties;
        this.submissionMapper = submissionMapper;
        this.efilingFilingPackageMapper = efilingFilingPackageMapper;
        this.efilingLookupService = efilingLookupService;
        this.efilingCourtService = efilingCourtService;
        this.efilingSubmissionService = efilingSubmissionService;
        this.documentStore = documentStore;
        this.bamboraPaymentAdapter = bamboraPaymentAdapter;
        this.sftpService = sftpService;
    }

    @Override
    public Submission generateFromRequest(UUID transactionId, UUID submissionId, UUID universalId, GenerateUrlRequest generateUrlRequest) {

        Optional<Submission> cachedSubmission = submissionStore.put(
                submissionMapper.toSubmission(
                        universalId,
                        submissionId,
                        transactionId,
                        generateUrlRequest,
                        toFilingPackage(generateUrlRequest),
                        getExpiryDate(),
                        isRushedSubmission(generateUrlRequest)));

        if (!cachedSubmission.isPresent())
            throw new StoreException("exception while storing submission object");

        return cachedSubmission.get();

    }

    private boolean isRushedSubmission(GenerateUrlRequest generateUrlRequest) {

        for (DocumentProperties documentProperties : generateUrlRequest.getFilingPackage().getDocuments()) {
            DocumentDetails documentDetails = documentStore.getDocumentDetails(generateUrlRequest.getFilingPackage().getCourt().getLevel(), generateUrlRequest.getFilingPackage().getCourt().getCourtClass(), documentProperties.getType());
            if (documentDetails.isRushRequired()) return true;
        }
        return false;
    }

    @Override
    public SubmitResponse createSubmission(Submission submission) {

        uploadFiles(submission);

        EfilingService service = efilingFilingPackageMapper.toEfilingService(submission);
        service.setEntryDateTime(DateUtils.getCurrentXmlDate());

        List<ca.bc.gov.open.jag.efilingapi.submission.models.Party> parties = new ArrayList();
        if (submission.getFilingPackage().getParties() != null)
            parties.addAll(submission.getFilingPackage().getParties());

        EfilingFilingPackage filingPackage = efilingFilingPackageMapper.toEfilingFilingPackage(submission,
                parties.stream()
                        .map(party -> efilingFilingPackageMapper.toEfilingParties(submission, party, String.valueOf(submission.getFilingPackage().getParties().indexOf(party))))
                        .collect(Collectors.toList()));
        filingPackage.setPackageControls(Arrays.asList(efilingFilingPackageMapper.toPackageAuthority(submission)));
        filingPackage.setDocuments(submission.getFilingPackage().getDocuments().stream()
                .map(document -> efilingFilingPackageMapper.toEfilingDocument(document, submission,
                        Arrays.asList(efilingFilingPackageMapper.toEfilingDocumentMilestone(document, submission)),
                        Arrays.asList(efilingFilingPackageMapper.toEfilingDocumentPayment(document, submission)),
                        Arrays.asList(efilingFilingPackageMapper.toEfilingDocumentStatus(document, submission)),
                        MessageFormat.format("fh_{0}_{1}_{2}", submission.getId(), submission.getUniversalId(), document.getName())))
                .collect(Collectors.toList()));
        filingPackage.setEntDtm(DateUtils.getCurrentXmlDate());
        filingPackage.setSubmitDtm(DateUtils.getCurrentXmlDate());
        SubmitResponse result = new SubmitResponse();
        result.transactionId(
                efilingSubmissionService
                        .submitFilingPackage(
                                service,
                                filingPackage,
                                submission.isRushedSubmission(),
                                efilingPayment -> bamboraPaymentAdapter.makePayment(efilingPayment)));
        return result;
    }

    @Override
    public Submission updateDocuments(Submission submission, UpdateDocumentRequest updateDocumentRequest) {

        updateDocumentRequest.getDocuments().stream().forEach(documentProperties -> {
            submission.getFilingPackage().addDocument(toDocument(
                    submission.getFilingPackage().getCourt().getLevel(),
                    submission.getFilingPackage().getCourt().getCourtClass(),
                    documentProperties));
        });

        submissionStore.put(submission);

        return submission;
    }

    private ca.bc.gov.open.jag.efilingapi.submission.models.FilingPackage toFilingPackage(GenerateUrlRequest request) {

        return ca.bc.gov.open.jag.efilingapi.submission.models.FilingPackage.builder()
                        .court(populateCourtDetails(request.getFilingPackage().getCourt()))
        .submissionFeeAmount(getSubmissionFeeAmount())
                .documents(request.getFilingPackage()
                .getDocuments()
                .stream()
                .map(documentProperties -> toDocument(
                        request.getFilingPackage().getCourt().getLevel(),
                        request.getFilingPackage().getCourt().getCourtClass(),
                        documentProperties))
                .collect(Collectors.toList())).create();

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

    private ca.bc.gov.open.jag.efilingapi.submission.models.Document toDocument(String courtLevel, String courtClass, DocumentProperties documentProperties) {

        DocumentDetails details = documentStore.getDocumentDetails(courtLevel, courtClass, documentProperties.getType());

        return
                ca.bc.gov.open.jag.efilingapi.submission.models.Document.builder()
                        .description(details.getDescription())
                        .statutoryFeeAmount(details.getStatutoryFeeAmount())
                        .type(documentProperties.getType())
                        .name(documentProperties.getName())
                        .mimeType(FileUtils.guessContentTypeFromName(documentProperties.getName()))
                        .isAmendment(documentProperties.getIsAmendment())
                        .isSupremeCourtScheduling(documentProperties.getIsSupremeCourtScheduling())
                        .subType(details.getOrderDocument() ? SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD : SubmissionConstants.SUBMISSION_ODOC_DOCUMENT_SUB_TYPE_CD)
                        .create();

    }

    private void uploadFiles(Submission submission) {
        submission.getFilingPackage().getDocuments().forEach(
                document ->
                        redisStoreToSftpStore(document.getName(), submission));

    }

    private void redisStoreToSftpStore(String fileName, Submission submission) {

        String compositeFileName = ca.bc.gov.open.jag.efilingapi.document.Document
                .builder()
                .userId(submission.getUniversalId())
                .transactionId(submission.getTransactionId())
                .submissionId(submission.getId())
                .fileName(fileName)
                .create().getCompositeId();


        sftpService.put(new ByteArrayInputStream(documentStore.get(compositeFileName)), MessageFormat.format("fh_{0}", compositeFileName));

        //Delete file from cache
        documentStore.evict(compositeFileName);

        // TODO: remove this is temp because of the SFTP rsync process
        try {
            Thread.sleep(Duration.ofSeconds(1).toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
