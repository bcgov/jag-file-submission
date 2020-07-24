package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.api.SubmissionApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.Document;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
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

    private final DocumentStore documentStore;



    public SubmissionApiDelegateImpl(
            SubmissionService submissionService,
            GenerateUrlResponseMapper generateUrlResponseMapper,
            NavigationProperties navigationProperties,
            SubmissionStore submissionStore, DocumentStore documentStore) {
        this.submissionService = submissionService;
        this.generateUrlResponseMapper = generateUrlResponseMapper;
        this.navigationProperties = navigationProperties;
        this.submissionStore = submissionStore;
        this.documentStore = documentStore;
    }

    @Override
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadSubmissionDocuments(UUID xAuthUserId, List<MultipartFile> files) {

        if (files == null || files.isEmpty())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_REQUIRED).create(),
                    HttpStatus.BAD_REQUEST);

        UUID submissionId = UUID.randomUUID();

        MDC.put(Keys.EFILING_SUBMISSION_ID, submissionId.toString());
        logger.info("new request for efiling {}", submissionId);

        try {

            for (MultipartFile file : files) {
                Document document = Document
                        .builder()
                        .owner(xAuthUserId)
                        .submissionId(submissionId)
                        .fileName(file.getResource().getFilename())
                        .content(file.getBytes())
                        .create();

                documentStore.put(document.getCompositeId(), document.getContent());
            }

        } catch (IOException e) {
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_STORAGE_FAILURE).create(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("{} stored in cache", files.size());

        MDC.remove(Keys.EFILING_SUBMISSION_ID);

        return ResponseEntity.ok(new UploadSubmissionDocumentsResponse().submissionId(submissionId).received(new BigDecimal(files.size())));

    }

    @Override
    public ResponseEntity<Resource> getSubmissionDocument(UUID xAuthUserId, UUID id, String filename) {


        MDC.put(Keys.EFILING_SUBMISSION_ID, id.toString());

        Document document = Document
                .builder()
                .owner(xAuthUserId)
                .submissionId(id)
                .fileName(filename)
                .create();

        byte[] bytes = documentStore.get(document.getCompositeId());

        if(bytes == null) return ResponseEntity.notFound().build();

        MDC.remove(Keys.EFILING_SUBMISSION_ID);

        return ResponseEntity.ok(new ByteArrayResource(bytes));

    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(UUID xAuthUserId, UUID id, GenerateUrlRequest generateUrlRequest) {

        MDC.put(Keys.EFILING_SUBMISSION_ID, id.toString());

        logger.info("Generate Url Request Received");

        ResponseEntity response;

        try {
            response = ResponseEntity.ok(
                    generateUrlResponseMapper.toGenerateUrlResponse(
                            submissionService.generateFromRequest(xAuthUserId, id, generateUrlRequest),
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
        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            response =  new ResponseEntity(buildEfilingError(ErrorResponse.DOCUMENT_TYPE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (StoreException e) {
            logger.warn(e.getMessage(), e);
            response =  new ResponseEntity(buildEfilingError(ErrorResponse.CACHE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MDC.remove(Keys.EFILING_SUBMISSION_ID);

        return response;

    }

    @Override
    public ResponseEntity<GetSubmissionResponse> getSubmission(UUID xAuthUserId, UUID id) {

        MDC.put(Keys.EFILING_SUBMISSION_ID, id.toString());

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(id, xAuthUserId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        GetSubmissionResponse response = new GetSubmissionResponse();

        response.setUserDetails(buildUserDetails(fromCacheSubmission.get()));

        response.setNavigation(fromCacheSubmission.get().getNavigation());

        MDC.remove(Keys.EFILING_SUBMISSION_ID);

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
    public ResponseEntity<FilingPackage> getSubmissionFilingPackage(UUID xAuthUserId, UUID id) {
        Optional<Submission> fromCacheSubmission = this.submissionStore.get(id, xAuthUserId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(fromCacheSubmission.get().getFilingPackage());
    }

    @Override
    @Secured
    public ResponseEntity<SubmitFilingPackageResponse> submit(UUID xAuthUserId, UUID id, SubmitFilingPackageRequest submitFilingPackageRequest) {
        ResponseEntity response;
        MDC.put(Keys.EFILING_SUBMISSION_ID, id.toString());
        //TODO: this will get the submission details from the cache and build a submission object
        try {
            SubmitFilingPackageResponse result = submissionService.submitFilingPackage(xAuthUserId, id, submitFilingPackageRequest);
            response = ResponseEntity.ok(result);
        } catch (EfilingSubmissionServiceException e) {
            response = new ResponseEntity(buildEfilingError(ErrorResponse.DOCUMENT_TYPE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        MDC.remove(Keys.EFILING_SUBMISSION_ID);

        return response;
    }

    public EfilingError buildEfilingError(ErrorResponse errorResponse) {

        EfilingError response = new EfilingError();
        response.setError(errorResponse.getErrorCode());
        response.setMessage(errorResponse.getErrorMessage());
        return response;

    }

}
