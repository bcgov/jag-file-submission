package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingaccountclient.exception.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingapi.api.SubmissionApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.fee.FeeService;
import ca.bc.gov.open.jag.efilingapi.fee.models.FeeRequest;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilinglookupclient.EfilingLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableConfigurationProperties(NavigationProperties.class)
public class SubmissionApiDelegateImpl implements SubmissionApiDelegate {

    public static final String EFILING_ROLE = "efiling";

    Logger logger = LoggerFactory.getLogger(SubmissionApiDelegateImpl.class);

    private final SubmissionService submissionService;

    private final NavigationProperties navigationProperties;

    private final CacheProperties cacheProperties;

    private final SubmissionMapper submissionMapper;

    private final EfilingAccountService efilingAccountService;

    private final EfilingLookupService efilingLookupService;

    private final FeeService feeService;

    public SubmissionApiDelegateImpl(
            SubmissionService submissionService,
            NavigationProperties navigationProperties,
            CacheProperties cacheProperties, SubmissionMapper submissionMapper,
            EfilingAccountService efilingAccountService, EfilingLookupService efilingLookupService,
            FeeService feeService) {
        this.submissionService = submissionService;
        this.navigationProperties = navigationProperties;
        this.cacheProperties = cacheProperties;
        this.submissionMapper = submissionMapper;
        this.efilingAccountService = efilingAccountService;
        this.efilingLookupService = efilingLookupService;
        this.feeService = feeService;
    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(@Valid GenerateUrlRequest generateUrlRequest) {

        logger.info("Generate Url Request Received");

        logger.debug("Attempting to get user cso account information");
        AccountDetails accountDetails;

        try {
            accountDetails = efilingAccountService.getAccountDetails(generateUrlRequest.getUserId());
        }
        catch (CSOHasMultipleAccountException e)   {
            return new ResponseEntity(buildEfilingError(ErrorResponse.ACCOUNTEXCEPTION), HttpStatus.BAD_REQUEST);
        }
        catch (NestedEjbException_Exception e) {
            return new ResponseEntity(buildEfilingError(ErrorResponse.GETPROFILESEXCEPTION), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Successfully got cso account information");

        if (accountDetails != null && !accountDetails.isEfileRolePresent()) {
            logger.warn("User does not have efiling role, therefore request is rejected.");
            return new ResponseEntity(buildEfilingError(ErrorResponse.INVALIDROLE), HttpStatus.FORBIDDEN);
        }

        //TODO: Replace with a service
        GenerateUrlResponse response = new GenerateUrlResponse();

        response.expiryDate(System.currentTimeMillis() + cacheProperties.getRedis().getTimeToLive().toMillis());

        Optional<Submission> cachedSubmission = submissionService.put(
                        submissionMapper.toSubmission(generateUrlRequest,
                        feeService.getFee(new FeeRequest(generateUrlRequest.getDocumentProperties().getType(),
                                generateUrlRequest.getDocumentProperties().getSubType())), accountDetails));

        if(!cachedSubmission.isPresent())
            return ResponseEntity.badRequest().body(null);

        response.setEfilingUrl(
                MessageFormat.format(
                               "{0}?submissionId={1}",
                                navigationProperties.getBaseUrl(),
                                cachedSubmission.get().getId()));

        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<GetSubmissionResponse> getSubmissionUserDetail(String id) {

        if(!isUUID(id)) {
            // TODO: add error reponse
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Submission> fromCacheSubmission = this.submissionService.getByKey(UUID.fromString(id));

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        GetSubmissionResponse response = new GetSubmissionResponse();

        response.setUserDetails(buildUserDetails(fromCacheSubmission.get()));

        response.setNavigation(fromCacheSubmission.get().getNavigation());

        return ResponseEntity.ok(response);

    }

    private UserDetails buildUserDetails(Submission submission) {

        UserDetails userDetails = new UserDetails();

        if(submission.getAccountDetails() != null) {
            Account account = new Account();
            account.setType(Account.TypeEnum.CSO);
            account.setIdentifier(submission.getAccountDetails().getAccountId().toString());
            userDetails.addAccountsItem(account);
            userDetails.setFirstName(submission.getAccountDetails().getFirstName());
            userDetails.setLastName(submission.getAccountDetails().getLastName());
            userDetails.setMiddleName(submission.getAccountDetails().getMiddleName());
            userDetails.setEmail(submission.getAccountDetails().getEmail());
        } else {
            userDetails.setFirstName("firstName");
            userDetails.setLastName("lastName");
            userDetails.setMiddleName("middleName");
            userDetails.setEmail("email");
        }

        return userDetails;

    }

    static boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public EfilingError buildEfilingError(ErrorResponse errorResponse) {
        EfilingError response = new EfilingError();
        response.setError(errorResponse.getErrorCode());
        response.setMessage(errorResponse.getErrorMessage());
        return response;
    }

    private BigDecimal getFee(String type) {
        try {
            return efilingLookupService.getServiceFee(type).getFeeAmt();
        } catch (DatatypeConfigurationException  e) {
            logger.error("Fee not found ", e);
            return BigDecimal.valueOf(0);
        }
    }

}
