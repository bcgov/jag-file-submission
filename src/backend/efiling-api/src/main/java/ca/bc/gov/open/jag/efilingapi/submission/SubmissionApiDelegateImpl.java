package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
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
import ca.bc.gov.open.jag.efilingapi.utils.SecurityUtils;
import ca.bc.gov.open.jag.efilingcommons.exceptions.*;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final AccountService accountService;

    private final GenerateUrlResponseMapper generateUrlResponseMapper;

    private final NavigationProperties navigationProperties;

    private final DocumentStore documentStore;


    public SubmissionApiDelegateImpl(
            SubmissionService submissionService,
            AccountService accountService,
            GenerateUrlResponseMapper generateUrlResponseMapper,
            NavigationProperties navigationProperties,
            SubmissionStore submissionStore, DocumentStore documentStore) {
        this.submissionService = submissionService;
        this.accountService = accountService;
        this.generateUrlResponseMapper = generateUrlResponseMapper;
        this.navigationProperties = navigationProperties;
        this.submissionStore = submissionStore;
        this.documentStore = documentStore;
    }

    @Override
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadSubmissionDocuments(UUID xTransactionId, List<MultipartFile> files) {

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
                        .transactionId(xTransactionId)
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
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getSubmissionDocument(UUID xTransactionId,
                                                          UUID submissionId,
                                                          String filename) {


        MDC.put(Keys.EFILING_SUBMISSION_ID, submissionId.toString());

        Document document = Document
                .builder()
                .transactionId(xTransactionId)
                .submissionId(submissionId)
                .fileName(filename)
                .create();

        byte[] bytes = documentStore.get(document.getCompositeId());

        if(bytes == null) return ResponseEntity.notFound().build();

        MDC.remove(Keys.EFILING_SUBMISSION_ID);

        return ResponseEntity.ok(new ByteArrayResource(bytes));

    }

    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(UUID xTransactionId, UUID submissionId, GenerateUrlRequest generateUrlRequest) {

        MDC.put(Keys.EFILING_SUBMISSION_ID, submissionId.toString());

        logger.info("Generate Url Request Received");

        ResponseEntity response;

        try {
            response = ResponseEntity.ok(
                    generateUrlResponseMapper.toGenerateUrlResponse(
                            submissionService.generateFromRequest(xTransactionId, submissionId, generateUrlRequest),
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
    @RolesAllowed("efiling-user")
    public ResponseEntity<GetSubmissionResponse> getSubmission(UUID submissionId, UUID xTransactionId) {

        Optional<UUID> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        MDC.put(Keys.EFILING_SUBMISSION_ID, submissionId.toString());

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();


        if (fromCacheSubmission.get().getAccountId() == null || fromCacheSubmission.get().getClientId() == null)
            setIdsInCachedSubmission(universalId.get(),fromCacheSubmission.get());


        GetSubmissionResponse response = new GetSubmissionResponse();

        response.setUserDetails(buildUserDetails(universalId.get()));

        response.setNavigation(fromCacheSubmission.get().getNavigation());

        MDC.remove(Keys.EFILING_SUBMISSION_ID);

        return ResponseEntity.ok(response);

    }

    private UserDetails buildUserDetails(UUID universalId) {

        UserDetails userDetails = new UserDetails();

        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);

        if(accountDetails != null) {

            if(accountDetails.isFileRolePresent()) {
                Account account = new Account();
                account.setType(Account.TypeEnum.CSO);
                account.setIdentifier(accountDetails.getAccountId().toString());
                userDetails.addAccountsItem(account);
            }

            userDetails.setUniversalId(accountDetails.getUniversalId());
            userDetails.setFirstName(accountDetails.getFirstName());
            userDetails.setLastName(accountDetails.getLastName());
            userDetails.setMiddleName(accountDetails.getMiddleName());
            userDetails.setEmail(accountDetails.getEmail());

        }

        return userDetails;

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<FilingPackage> getSubmissionFilingPackage(UUID xTransactionId, UUID submissionId) {
        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(fromCacheSubmission.get().getFilingPackage());
    }


    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<CreateServiceResponse> createService(UUID xTransactionId,
                                                              UUID submissionId, Object body) {
        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();
        ResponseEntity response;
        MDC.put(Keys.EFILING_SUBMISSION_ID, submissionId.toString());
        try {
            CreateServiceResponse result = submissionService.createSubmission(fromCacheSubmission.get());
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

    private void setIdsInCachedSubmission(UUID universalId, Submission submission) {
        AccountDetails accountDetails = accountService.getCsoAccountDetails(universalId);
        if (accountDetails != null) {
            submission.setAccountId(accountDetails.getAccountId());
            submission.setClientId(accountDetails.getClientId());
            this.submissionStore.put(submission);
        }
    }
}
