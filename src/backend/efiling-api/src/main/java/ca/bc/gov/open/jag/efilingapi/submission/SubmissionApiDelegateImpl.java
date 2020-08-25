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
import ca.bc.gov.open.jag.efilingapi.utils.MdcUtils;
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
    @RolesAllowed("efiling-client")
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadSubmissionDocuments(UUID xTransactionId, String xUserId, List<MultipartFile> files) {

        UUID submissionId = UUID.randomUUID();

        MdcUtils.setClientMDC(submissionId, xTransactionId);

        logger.info("attempting to upload original document [{}]", submissionId);

        Optional<UUID> actualUserId = SecurityUtils.stringToUUID(xUserId);

        if (!actualUserId.isPresent())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);

        ResponseEntity response = storeDocuments(submissionId, xTransactionId, actualUserId.get(), files);

        logger.info("successfully uploaded original document [{}]", submissionId);

        MdcUtils.clearClientMDC();

        return response;

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadAdditionalSubmissionDocuments(UUID submissionId, UUID xTransactionId, List<MultipartFile> files) {

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        MdcUtils.setUserMDC(submissionId, xTransactionId);

        logger.info("attempting to upload new document for transaction [{}]", submissionId);

        ResponseEntity responseEntity = storeDocuments(submissionId, xTransactionId, fromCacheSubmission.get().getUniversalId(), files);

        logger.info("successfully uploaded new document for transaction [{}]", submissionId);

        MdcUtils.clearUserMDC();

        return responseEntity;
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<UpdateDocumentResponse> updateDocumentProperties(UUID submissionId, UUID xTransactionId, UpdateDocumentRequest updateDocumentRequest) {

        MdcUtils.setUserMDC(submissionId, xTransactionId);

        logger.info("attempting to add new document for transaction [{}]", submissionId);

        if (updateDocumentRequest == null || updateDocumentRequest.getDocuments().isEmpty())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_REQUIRED).create(),
                    HttpStatus.BAD_REQUEST);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();


        try {
            Submission submission = submissionService.updateDocuments(fromCacheSubmission.get(), updateDocumentRequest);
            submissionStore.put(submission);
            UpdateDocumentResponse updateDocumentResponse = new UpdateDocumentResponse();
            updateDocumentResponse.setDocuments(submission.getFilingPackage().getDocuments());
            logger.info("successfully added new document for transaction [{}]", submissionId);
            return ResponseEntity.ok(updateDocumentResponse);
        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            return new ResponseEntity(buildEfilingError(ErrorResponse.DOCUMENT_TYPE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            MdcUtils.clearUserMDC();
        }
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getSubmissionDocument(UUID xTransactionId,
                                                          UUID submissionId,
                                                          String filename) {

        logger.info("getSubmission document for transaction [{}]", xTransactionId);

        MdcUtils.setUserMDC(submissionId, xTransactionId);

        Document document = Document
                .builder()
                .transactionId(xTransactionId)
                .submissionId(submissionId)
                .fileName(filename)
                .create();

        byte[] bytes = documentStore.get(document.getCompositeId());

        if(bytes == null) return ResponseEntity.notFound().build();

        logger.info("successfully retrieved document for transaction [{}]", xTransactionId);

        MdcUtils.clearUserMDC();

        return ResponseEntity.ok(new ByteArrayResource(bytes));

    }

    @Override
    @RolesAllowed("efiling-client")
    public ResponseEntity<GenerateUrlResponse> generateUrl(UUID xTransactionId, String xUserId, UUID submissionId, GenerateUrlRequest generateUrlRequest) {

        MdcUtils.setClientMDC(xTransactionId, submissionId);

        logger.info("Attempting to generate Url Request Received");

        Optional<UUID> actualUserId = SecurityUtils.stringToUUID(xUserId);

        if (!actualUserId.isPresent())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);

        ResponseEntity response;

        try {
            generateUrlRequest.getClientApplication().setType(SecurityUtils.getApplicationCode());
            response = ResponseEntity.ok(
                    generateUrlResponseMapper.toGenerateUrlResponse(
                            submissionService.generateFromRequest(xTransactionId, submissionId, actualUserId.get(), generateUrlRequest),
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

        MdcUtils.clearClientMDC();

        return response;

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<GetSubmissionResponse> getSubmission(UUID submissionId, UUID xTransactionId) {

        Optional<UUID> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        MdcUtils.setUserMDC(submissionId, xTransactionId);

        logger.info("attempting to get Submission for transactionId [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();


        if (fromCacheSubmission.get().getAccountDetails() == null ||
            fromCacheSubmission.get().getAccountDetails().getAccountId() == null ||
            fromCacheSubmission.get().getAccountDetails().getClientId() == null)
            setIdsInCachedSubmission(universalId.get(),fromCacheSubmission.get());


        GetSubmissionResponse response = new GetSubmissionResponse();

        UserDetails userDetails = buildUserDetails(universalId.get());

        if(userDetails.getAccounts() == null || userDetails.getAccounts().isEmpty()) logger.info("User does not have a CSO account");

        response.setUserDetails(userDetails);

        response.setClientApplication(fromCacheSubmission.get().getClientApplication());

        response.setNavigation(fromCacheSubmission.get().getNavigation());

        logger.info("Successfully retrieved submission for transactionId [{}]", xTransactionId);

        MdcUtils.clearUserMDC();

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
            userDetails.setCardRegistered(accountDetails.isCardRegistered());

        }

        return userDetails;

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<FilingPackage> getSubmissionFilingPackage(UUID xTransactionId, UUID submissionId) {

        MdcUtils.setUserMDC(submissionId, xTransactionId);

        logger.info("attempting to get Submission Filing Package for transactionId [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        logger.info("successfully retrieved submission filing package for transactionId [{}]", xTransactionId);

        MdcUtils.clearUserMDC();

        return ResponseEntity.ok(fromCacheSubmission.get().getFilingPackage());
    }


    @Override
    public ResponseEntity<Void> deleteSubmission(UUID submissionId, UUID xTransactionId) {

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);
        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();
        //Remove documents from cache
        if (fromCacheSubmission.get().getFilingPackage() != null && fromCacheSubmission.get().getFilingPackage().getDocuments() != null)
            fromCacheSubmission.get().getFilingPackage().getDocuments().forEach(
                    document -> documentStore.evict(
                            Document
                                    .builder()
                                    .userId(fromCacheSubmission.get().getUniversalId())
                                    .submissionId(submissionId)
                                    .fileName(document.getName())
                                    .create().getCompositeId()));

        //Remove submission from cache
        submissionStore.evict(submissionId, xTransactionId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<SubmitResponse> submit(UUID xTransactionId, UUID submissionId, Object body) {

        MdcUtils.setUserMDC(submissionId, xTransactionId);

        logger.info("attempting to submit efiling package for transaction [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionId, xTransactionId);

        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();
        ResponseEntity response;
        MDC.put(Keys.MDC_EFILING_SUBMISSION_ID, submissionId.toString());
        try {
            SubmitResponse result = submissionService.createSubmission(fromCacheSubmission.get());
            response = new ResponseEntity(result, HttpStatus.CREATED);
        } catch (EfilingSubmissionServiceException e) {
            response = new ResponseEntity(buildEfilingError(ErrorResponse.DOCUMENT_TYPE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("successfully submitted efiling package for transaction [{}]", xTransactionId);

        MdcUtils.clearUserMDC();

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
            submission.setAccountDetails(accountDetails);
            this.submissionStore.put(submission);
        }
    }

    private ResponseEntity storeDocuments(UUID submissionId, UUID xTransactionId, UUID xUserId, List<MultipartFile> files) {

        if (files == null || files.isEmpty())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_REQUIRED).create(),
                    HttpStatus.BAD_REQUEST);

        try {

            for (MultipartFile file : files) {
                Document document = Document
                        .builder()
                        .userId(xUserId)
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

        return ResponseEntity.ok(new UploadSubmissionDocumentsResponse().submissionId(submissionId).received(new BigDecimal(files.size())));
    }

}
