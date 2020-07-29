package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingapi.utils.FileUtils;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingAccountService;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingSubmissionService;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubmissionServiceImpl implements SubmissionService {

    Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    public static final UUID FAKE_ACCOUNT = UUID.fromString("88da92db-0791-491e-8c58-1a969e67d2fb");

    private final SubmissionStore submissionStore;

    private final CacheProperties cacheProperties;

    private final SubmissionMapper submissionMapper;

    private final EfilingAccountService efilingAccountService;

    private final EfilingLookupService efilingLookupService;

    private final EfilingCourtService efilingCourtService;

    private final EfilingSubmissionService efilingSubmissionService;

    private final DocumentStore documentStore;

    public SubmissionServiceImpl(
            SubmissionStore submissionStore,
            CacheProperties cacheProperties,
            SubmissionMapper submissionMapper,
            EfilingAccountService efilingAccountService,
            EfilingLookupService efilingLookupService,
            EfilingCourtService efilingCourtService, EfilingSubmissionService efilingSubmissionService, DocumentStore documentStore) {
        this.submissionStore = submissionStore;
        this.cacheProperties = cacheProperties;
        this.submissionMapper = submissionMapper;
        this.efilingAccountService = efilingAccountService;
        this.efilingLookupService = efilingLookupService;
        this.efilingCourtService = efilingCourtService;
        this.efilingSubmissionService = efilingSubmissionService;
        this.documentStore = documentStore;
    }

    @Override
    public Submission generateFromRequest(UUID authUserId, UUID submissionId, GenerateUrlRequest generateUrlRequest) {

        logger.debug("Attempting to get user cso account information");
        AccountDetails accountDetails = efilingAccountService.getAccountDetails(authUserId, "Individual");
        logger.info("Successfully got cso account information");

        if (accountDetails != null && accountDetails.getAccountId() != null && !accountDetails.isFileRolePresent()) {
            throw new InvalidAccountStateException("Account does not have CSO FILE ROLE");
        } else if (accountDetails == null) {
            accountDetails = fakeFromBceId(authUserId);
        }

        Optional<Submission> cachedSubmission = submissionStore.put(
                submissionMapper.toSubmission(
                        submissionId,
                        generateUrlRequest,
                        toFilingPackage(generateUrlRequest),
                        accountDetails,
                        getExpiryDate()));

        if(!cachedSubmission.isPresent())
            throw new StoreException("exception while storing submission object");

        return cachedSubmission.get();

    }

    @Override
    public SubmitFilingPackageResponse submitFilingPackage(UUID authUserId, UUID submissionId, SubmitFilingPackageRequest submitFilingPackageRequest) {
        SubmitFilingPackageResponse result = new SubmitFilingPackageResponse();
        //TODO: create a pull submitting model
        result.setTransactionId(efilingSubmissionService.submitFilingPackage(submissionId));
        result.setAcknowledge(LocalDate.now());
        return result;
    }

    private AccountDetails fakeFromBceId(UUID authUserId) {

        // TODO: implement account details service

        logger.error("THIS IS FOR TESTING ONLY");

        if(authUserId.equals(FAKE_ACCOUNT))
            return new AccountDetails(authUserId, BigDecimal.ZERO, BigDecimal.ZERO, false, "Bob", "Ross", "Rob", "bross@paintit.com");

        return null;

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

        return document;

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
