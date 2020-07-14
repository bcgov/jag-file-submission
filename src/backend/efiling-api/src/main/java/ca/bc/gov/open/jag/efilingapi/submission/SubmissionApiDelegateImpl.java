package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.api.SubmissionApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@EnableConfigurationProperties(NavigationProperties.class)
public class SubmissionApiDelegateImpl implements SubmissionApiDelegate {

    Logger logger = LoggerFactory.getLogger(SubmissionApiDelegateImpl.class);

    private final SubmissionService submissionService;

    private final SubmissionStore submissionStore;

    private final GenerateUrlResponseMapper generateUrlResponseMapper;

    private final NavigationProperties navigationProperties;


    public SubmissionApiDelegateImpl(
            SubmissionService submissionService,
            GenerateUrlResponseMapper generateUrlResponseMapper,
            NavigationProperties navigationProperties,
            SubmissionStore submissionStore) {
        this.submissionService = submissionService;
        this.generateUrlResponseMapper = generateUrlResponseMapper;
        this.navigationProperties = navigationProperties;
        this.submissionStore = submissionStore;
    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(UUID xAuthUserId, GenerateUrlRequest generateUrlRequest) {
        logger.info("Generate Url Request Received");

        ResponseEntity response;

        try {
            response = ResponseEntity.ok(
                    generateUrlResponseMapper.toGenerateUrlResponse(
                            submissionService.generateFromRequest(xAuthUserId, generateUrlRequest),
                            navigationProperties.getBaseUrl()));
            logger.info("successfully generated return url.");
        }
        catch (CSOHasMultipleAccountException e)   {
            logger.warn(e.getMessage(), e);
            response =  new ResponseEntity(buildEfilingError(ErrorResponse.ACCOUNTEXCEPTION), HttpStatus.BAD_REQUEST);
        }
        catch (InvalidAccountStateException e) {
            logger.warn(e.getMessage(), e);
            response =  new ResponseEntity(buildEfilingError(ErrorResponse.INVALIDROLE), HttpStatus.FORBIDDEN);
        }
        catch (StoreException e) {
            logger.warn(e.getMessage(), e);
            response =  new ResponseEntity(buildEfilingError(ErrorResponse.CACHE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;

    }

    @Override
    public ResponseEntity<GetSubmissionResponse> getSubmission(UUID id) {

        Optional<Submission> fromCacheSubmission = this.submissionStore.getByKey(id);

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

            if(submission.getAccountDetails().isFileRolePresent()) {
                Account account = new Account();
                account.setType(Account.TypeEnum.CSO);
                account.setIdentifier(submission.getAccountDetails().getAccountId().toString());
                userDetails.addAccountsItem(account);
            }

            userDetails.setUniversalId(submission.getAccountDetails().getUniversalId());
            userDetails.setFirstName(submission.getAccountDetails().getFirstName());
            userDetails.setLastName(submission.getAccountDetails().getLastName());
            userDetails.setMiddleName(submission.getAccountDetails().getMiddleName());
            userDetails.setEmail(submission.getAccountDetails().getEmail());

        } else {

            // TODO: remove this
            userDetails.setFirstName("firstName");
            userDetails.setLastName("lastName");
            userDetails.setMiddleName("middleName");
            userDetails.setEmail("email");

        }

        return userDetails;

    }

    @Override
    public ResponseEntity<GetPacakageInformationResponse> getSubmissionPackage(UUID id) {
        Optional<Submission> fromCacheSubmission = this.submissionStore.getByKey(id);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        GetPacakageInformationResponse response = new GetPacakageInformationResponse();
        response.setClientApplication(fromCacheSubmission.get().getClientApplication());
        return ResponseEntity.ok(response);
    }

    public EfilingError buildEfilingError(ErrorResponse errorResponse) {
        EfilingError response = new EfilingError();
        response.setError(errorResponse.getErrorCode());
        response.setMessage(errorResponse.getErrorMessage());
        return response;
    }

}
