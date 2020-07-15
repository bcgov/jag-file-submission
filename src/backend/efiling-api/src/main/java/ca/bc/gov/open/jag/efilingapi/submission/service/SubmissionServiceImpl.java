package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.fee.FeeService;
import ca.bc.gov.open.jag.efilingapi.fee.models.FeeRequest;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;
import java.util.List;
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

    private final FeeService feeService;

    public SubmissionServiceImpl(
            SubmissionStore submissionStore,
            CacheProperties cacheProperties,
            SubmissionMapper submissionMapper,
            EfilingAccountService efilingAccountService,
            FeeService feeService) {
        this.submissionStore = submissionStore;
        this.cacheProperties = cacheProperties;
        this.submissionMapper = submissionMapper;
        this.efilingAccountService = efilingAccountService;
        this.feeService = feeService;
    }

    @Override
    public Submission generateFromRequest(UUID authUserId, GenerateUrlRequest generateUrlRequest) {

        logger.debug("Attempting to get user cso account information");
        AccountDetails accountDetails = efilingAccountService.getAccountDetails(authUserId, "Individual");
        logger.info("Successfully got cso account information");

        if (accountDetails != null && accountDetails.getAccountId() != null && !accountDetails.isFileRolePresent()) {
            throw new InvalidAccountStateException("Account does not have CSO FILE ROLE");
        } else if (accountDetails == null) {
            accountDetails = fakeFromBceId(authUserId);
        }



        Optional<Submission> cachedSubmission = submissionStore.put(
                submissionMapper.toSubmission(generateUrlRequest,
                        getFees(generateUrlRequest.getPackage().getDocuments()),
                        accountDetails,
                        getExpiryDate()));

        if(!cachedSubmission.isPresent())
            throw new StoreException("exception while storing submission object");

        return cachedSubmission.get();

    }

    private AccountDetails fakeFromBceId(UUID authUserId) {

        // TODO: implement account details service

        logger.error("THIS IS FOR TESTING ONLY");

        if(authUserId.equals(FAKE_ACCOUNT))
            return new AccountDetails(authUserId, BigDecimal.ZERO, BigDecimal.ZERO, false, "Bob", "Ross", "Rob", "bross@paintit.com");

        return null;

    }

    private List<Fee> getFees(List<DocumentProperties> documents) {
        return  documents.stream()
                .map(doc -> feeService.getFee(
                                new FeeRequest(
                                        doc.getType())))
                .collect(Collectors.toList());
    }

    private long getExpiryDate() {
        return System.currentTimeMillis() + cacheProperties.getRedis().getTimeToLive().toMillis();
    }


}
