package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountDetails;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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

    private final FeeService feeService;

    private final EfilingAccountService efilingAccountService;


    public SubmissionApiDelegateImpl(
            SubmissionService submissionService,
            NavigationProperties navigationProperties,
            CacheProperties cacheProperties, SubmissionMapper submissionMapper, FeeService feeService,
            EfilingAccountService efilingAccountService) {
        this.submissionService = submissionService;
        this.navigationProperties = navigationProperties;
        this.cacheProperties = cacheProperties;
        this.submissionMapper = submissionMapper;
        this.feeService = feeService;
        this.efilingAccountService = efilingAccountService;
    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(@Valid GenerateUrlRequest generateUrlRequest) {

        logger.info("Generate Url Request Received");

        logger.debug("Attempting to get user cso account information");
        CsoAccountDetails csoAccountDetails;

        try {
            csoAccountDetails = efilingAccountService.getAccountDetails(generateUrlRequest.getUserId());
        }
        catch (CSOHasMultipleAccountException e)   {
            return new ResponseEntity(buildEfilingError(ErrorResponse.ACCOUNTEXCEPTION), HttpStatus.BAD_REQUEST);
        }
        catch (NestedEjbException_Exception e) {
            return new ResponseEntity(buildEfilingError(ErrorResponse.GETPROFILESEXCEPTION), HttpStatus.BAD_REQUEST);
        }

        logger.info("Successfully got cso account information");

        if (csoAccountDetails != null && !csoAccountDetails.getHasEfileRole()) {
            logger.warn("User does not have efiling role, therefore request is rejected.");
            return new ResponseEntity(buildEfilingError(ErrorResponse.INVALIDROLE), HttpStatus.FORBIDDEN);
        }

        //TODO: Replace with a service
        GenerateUrlResponse response = new GenerateUrlResponse();

        response.expiryDate(System.currentTimeMillis() + cacheProperties.getRedis().getTimeToLive().toMillis());

        Optional<Submission> cachedSubmission = submissionService.put(
                        submissionMapper.toSubmission(generateUrlRequest,
                        feeService.getFee(new FeeRequest(generateUrlRequest.getDocumentProperties().getType(),
                                generateUrlRequest.getDocumentProperties().getSubType())), csoAccountDetails));

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
    public ResponseEntity<UserDetail> getSubmissionUserDetail(String id) {


        if(!isUUID(id)) {
            // TODO: add error reponse
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Submission> fromCacheSubmission = this.submissionService.getByKey(UUID.fromString(id));

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        UserDetail response = new UserDetail();
        response.setCsoAccountExists(fromCacheSubmission.get().getCsoAccountDetails() != null);

        response.setNavigation(fromCacheSubmission.get().getNavigation());

        return ResponseEntity.ok(response);

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

}
