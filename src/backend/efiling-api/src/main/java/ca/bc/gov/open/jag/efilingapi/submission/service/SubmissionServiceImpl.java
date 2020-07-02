package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
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

import java.util.Optional;
import java.util.UUID;

public class SubmissionServiceImpl implements SubmissionService {

    Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);

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
    public Submission generateFromRequest(GenerateUrlRequest generateUrlRequest) throws NestedEjbException_Exception {

        logger.debug("Attempting to get user cso account information");
        AccountDetails accountDetails = efilingAccountService.getAccountDetails(formatUserGuid(generateUrlRequest.getUserId()), "");
        logger.info("Successfully got cso account information");

        if (accountDetails != null && !accountDetails.isFileRolePresent()) {
            throw new InvalidAccountStateException("Account does not have CSO FILE ROLE");
        }

        Optional<Submission> cachedSubmission = submissionStore.put(
                submissionMapper.toSubmission(generateUrlRequest,
                        feeService.getFee(
                                new FeeRequest(
                                        generateUrlRequest.getDocumentProperties().getType(),
                                        generateUrlRequest.getDocumentProperties().getSubType())),
                        accountDetails,
                        getExpiryDate()));

        if(!cachedSubmission.isPresent())
            throw new StoreException("exception while storing submission object");

        return cachedSubmission.get();

    }

    private String formatUserGuid(UUID id) {
        return id.toString().replace("-", "").toUpperCase();
    }

    private long getExpiryDate() {
        return System.currentTimeMillis() + cacheProperties.getRedis().getTimeToLive().toMillis();
    }


}
