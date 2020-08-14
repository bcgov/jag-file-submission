package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.payment.BamboraPaymentAdapter;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.EfilingFilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
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
            BamboraPaymentAdapter bamboraPaymentAdapter, SftpService sftpService) {
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
    public Submission generateFromRequest(UUID transactionId, UUID submissionId, GenerateUrlRequest generateUrlRequest) {

        Optional<Submission> cachedSubmission = submissionStore.put(
                submissionMapper.toSubmission(
                        submissionId,
                        transactionId,
                        generateUrlRequest,
                        toFilingPackage(generateUrlRequest),
                        getExpiryDate()));

        if(!cachedSubmission.isPresent())
            throw new StoreException("exception while storing submission object");

        return cachedSubmission.get();

    }

    @Override
    public SubmitResponse createSubmission(Submission submission) {

        uploadFiles(submission);

        EfilingService service = efilingFilingPackageMapper.toEfilingService(submission);
        service.setEntryDateTime(DateUtils.getCurrentXmlDate());

        EfilingFilingPackage filingPackage = efilingFilingPackageMapper.toEfilingFilingPackage(submission);
        filingPackage.setPackageControls(Arrays.asList(efilingFilingPackageMapper.toPackageAuthority(submission)));
        filingPackage.setDocuments(submission.getFilingPackage().getDocuments().stream()
                                        .map(document -> efilingFilingPackageMapper.toEfilingDocument(document))
                                        .collect(Collectors.toList()));
        filingPackage.setEntDtm(DateUtils.getCurrentXmlDate());

        SubmitResponse result = new SubmitResponse();
        result.transactionId(efilingSubmissionService.submitFilingPackage(service, filingPackage, (efilingPayment) -> {
            return bamboraPaymentAdapter.makePayment(efilingPayment);
        }));
        return result;
    }

    @Override
    public Submission updateDocuments(Submission submission, UpdateDocumentRequest updateDocumentRequest) {

        submission.getFilingPackage().getDocuments()
                .addAll(
                        updateDocumentRequest.getDocuments()
                        .stream()
                        .map(documentProperties -> toDocument(
                            submission.getFilingPackage().getCourt().getLevel(),
                            submission.getFilingPackage().getCourt().getCourtClass(),
                            documentProperties))
                        .collect(Collectors.toList()));
        return submission;
    }

    private FilingPackage toFilingPackage(GenerateUrlRequest request) {

        FilingPackage filingPackage = new FilingPackage();
        filingPackage.setCourt(populateCourtDetails(request.getFilingPackage().getCourt()));
        filingPackage.setSubmissionFeeAmount(getSubmissionFeeAmount(request));
        filingPackage.setDocuments(request.getFilingPackage()
                .getDocuments()
                .stream()
                .map(documentProperties -> toDocument(
                        request.getFilingPackage().getCourt().getLevel(),
                        request.getFilingPackage().getCourt().getCourtClass(),
                        documentProperties))
                .collect(Collectors.toList()));
        return filingPackage;

    }

    private Court populateCourtDetails(CourtBase courtBase) {
        Court court = new Court();
        CourtDetails courtDetails = efilingCourtService.getCourtDescription(courtBase.getLocation());
        court.setLocation(courtBase.getLocation());
        court.setLevel(courtBase.getLevel());
        court.setCourtClass(courtBase.getCourtClass());
        court.setDivision(courtBase.getDivision());
        court.setFileNumber(courtBase.getFileNumber());

        court.setLocationDescription(courtDetails.getCourtDescription());
        court.setClassDescription(courtDetails.getClassDescription());
        court.setLevelDescription(courtDetails.getLevelDescription());
        return court;
    }

    private Document toDocument(String courtLevel, String courtClass, DocumentProperties documentProperties) {

        Document document = new Document();

        DocumentDetails details = documentStore.getDocumentDetails(courtLevel, courtClass, documentProperties.getType());

        document.setDescription(details.getDescription());
        document.setStatutoryFeeAmount(details.getStatutoryFeeAmount());
        document.setType(documentProperties.getType());
        document.setName(documentProperties.getName());
        document.setMimeType(FileUtils.guessContentTypeFromName(documentProperties.getName()));
        document.setIsAmendment(documentProperties.getIsAmendment());
        document.setIsSupremeCourtScheduling(documentProperties.getIsSupremeCourtScheduling());

        return document;

    }

    private void uploadFiles(Submission submission) {
        submission.getFilingPackage().getDocuments().forEach(
                document -> redisStoreToSftpStore(documentStore.get(MessageFormat.format("{0}_{1}_{2}",submission.getTransactionId(),submission.getId(),document.getName())), document.getName(), submission));

    }

    private void redisStoreToSftpStore(byte[] inFile, String fileName, Submission submission) {

        String newFileName = MessageFormat.format("fh_{0}_{1}_{2}", submission.getId(), submission.getTransactionId(), fileName);

        sftpService.put(new ByteArrayInputStream(inFile), newFileName);

    }

    private BigDecimal getSubmissionFeeAmount(GenerateUrlRequest request) {

        request.getClientApplication().setType(SubmissionConstants.SUBMISSION_FEE_TYPE);
        ServiceFees fee = efilingLookupService.getServiceFee(SubmissionConstants.SUBMISSION_FEE_TYPE);
        return fee == null ? BigDecimal.ZERO : fee.getFeeAmount();

    }

    private long getExpiryDate() {
        return System.currentTimeMillis() + cacheProperties.getRedis().getTimeToLive().toMillis();
    }

}
