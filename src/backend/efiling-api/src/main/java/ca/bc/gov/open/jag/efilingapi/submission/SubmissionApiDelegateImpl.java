package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.SubmissionApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import ca.bc.gov.open.jag.efilingapi.utils.TikaAnalysis;
import ca.bc.gov.open.jag.efilingcommons.exceptions.*;
import org.apache.commons.lang3.StringUtils;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableConfigurationProperties(NavigationProperties.class)
public class SubmissionApiDelegateImpl implements SubmissionApiDelegate {

    private static final String UNIVERSAL_ID_IS_REQUIRED = "universal Id is required.";
    Logger logger = LoggerFactory.getLogger(SubmissionApiDelegateImpl.class);

    private final SubmissionService submissionService;
    private final SubmissionStore submissionStore;
    private final AccountService accountService;
    private final GenerateUrlResponseMapper generateUrlResponseMapper;
    private final NavigationProperties navigationProperties;
    private final DocumentStore documentStore;
    private final ClamAvService clamAvService;
    private final FilingPackageMapper filingPackageMapper;
    private final GenerateUrlRequestValidator generateUrlRequestValidator;

    public SubmissionApiDelegateImpl(
            SubmissionService submissionService,
            AccountService accountService,
            GenerateUrlResponseMapper generateUrlResponseMapper,
            NavigationProperties navigationProperties,
            SubmissionStore submissionStore, DocumentStore documentStore, ClamAvService clamAvService, FilingPackageMapper filingPackageMapper, GenerateUrlRequestValidator generateUrlRequestValidator) {
        this.submissionService = submissionService;
        this.accountService = accountService;
        this.generateUrlResponseMapper = generateUrlResponseMapper;
        this.navigationProperties = navigationProperties;
        this.submissionStore = submissionStore;
        this.documentStore = documentStore;
        this.clamAvService = clamAvService;
        this.filingPackageMapper = filingPackageMapper;
        this.generateUrlRequestValidator = generateUrlRequestValidator;
    }

    @Override
    @RolesAllowed({"efiling-client", "efiling-admin"})
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadSubmissionDocuments(UUID xTransactionId, String xUserId, List<MultipartFile> files) {

        if(StringUtils.isBlank(xUserId)) {
            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }

        SubmissionKey submissionKey = new SubmissionKey(
                xUserId,
                xTransactionId,
                UUID.randomUUID());

        logger.info("attempting to upload original document [{}]", submissionKey.getSubmissionId());

        ResponseEntity response = storeDocuments(submissionKey, files);

        logger.info("successfully uploaded original document [{}]", submissionKey.getSubmissionId());

        return response;

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadAdditionalSubmissionDocuments(UUID submissionId, UUID xTransactionId, List<MultipartFile> files) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        logger.info("attempting to upload new document for transaction [{}]", submissionId);

        ResponseEntity responseEntity = storeDocuments(submissionKey, files);

        logger.info("successfully uploaded new document for transaction [{}]", submissionId);

        return responseEntity;
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<UpdateDocumentResponse> updateDocumentProperties(UUID submissionId, UUID
            xTransactionId, UpdateDocumentRequest updateDocumentRequest) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }


        logger.info("attempting to add new document for transaction [{}]", submissionId);

        if (updateDocumentRequest == null || updateDocumentRequest.getDocuments().isEmpty())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_REQUIRED).create(),
                    HttpStatus.BAD_REQUEST);

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        try {
            Submission submission = submissionService.updateDocuments(fromCacheSubmission.get(), updateDocumentRequest, submissionKey);
            UpdateDocumentResponse updateDocumentResponse = new UpdateDocumentResponse();
            FilingPackage filingPackage = filingPackageMapper.toApiFilingPackage(submission.getFilingPackage());
            updateDocumentResponse.setDocuments(filingPackage.getDocuments());
            logger.info("successfully added new document for transaction [{}]", submissionId);
            return ResponseEntity.ok(updateDocumentResponse);

        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            return new ResponseEntity(buildEfilingError(ErrorResponse.DOCUMENT_TYPE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getSubmissionDocument(UUID xTransactionId,
                                                          UUID submissionId,
                                                          String filename) {


        logger.info("getSubmission document for transaction [{}]", xTransactionId);

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        byte[] bytes = documentStore.get(submissionKey, filename);

        if (bytes == null) return ResponseEntity.notFound().build();

        logger.info("successfully retrieved document for transaction [{}]", xTransactionId);

        return ResponseEntity.ok(new ByteArrayResource(bytes));

    }

    @Override
    @RolesAllowed({"efiling-client", "efiling-admin"})
    public ResponseEntity<GenerateUrlResponse> generateUrl(UUID xTransactionId, String xUserId, UUID submissionId, GenerateUrlRequest generateUrlRequest) {


        logger.info("Attempting to generate Url Request Received");

        if (StringUtils.isBlank(xUserId)) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }

        Optional<String> applicationCode = SecurityUtils.getApplicationCode();

        if (!applicationCode.isPresent()) {

            logger.error("[{}]: {}", ErrorResponse.MISSING_APPLICATION_CODE.getErrorCode(), ErrorResponse.MISSING_APPLICATION_CODE.getErrorMessage());
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_APPLICATION_CODE).create(),
                    HttpStatus.FORBIDDEN
            );

        }

        Notification validation = generateUrlRequestValidator.validate(generateUrlRequest, applicationCode.get());

        if (validation.hasError())
            return new ResponseEntity(EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALID_INITIAL_SUBMISSION_PAYLOAD).addDetails(validation.getErrors()).create(),
                    HttpStatus.BAD_REQUEST);


        if (accountService.getCsoAccountDetails(xUserId) != null &&
                !accountService.getCsoAccountDetails(xUserId).isFileRolePresent())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDROLE).create(),
                    HttpStatus.FORBIDDEN);

        SubmissionKey submissionKey = new SubmissionKey(xUserId, xTransactionId, submissionId);

        ResponseEntity response;

        try {
            response = ResponseEntity.ok(
                    generateUrlResponseMapper.toGenerateUrlResponse(
                            submissionService.generateFromRequest(applicationCode.get(), submissionKey, generateUrlRequest),
                            navigationProperties.getBaseUrl()));
            logger.info("successfully generated return url.");
        } catch (CSOHasMultipleAccountException e) {
            logger.warn(e.getMessage(), e);
            response = new ResponseEntity(buildEfilingError(ErrorResponse.ACCOUNTEXCEPTION), HttpStatus.BAD_REQUEST);
        } catch (InvalidAccountStateException e) {
            logger.warn(e.getMessage(), e);
            response = new ResponseEntity(buildEfilingError(ErrorResponse.INVALIDROLE), HttpStatus.FORBIDDEN);
        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            response = new ResponseEntity(buildEfilingError(ErrorResponse.DOCUMENT_TYPE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (StoreException e) {
            logger.warn(e.getMessage(), e);
            response = new ResponseEntity(buildEfilingError(ErrorResponse.CACHE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<GetSubmissionConfigResponse> getSubmissionConfig(UUID submissionId, UUID xTransactionId) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if (!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);
        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        logger.info("attempting to get Submission for transactionId [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        GetSubmissionConfigResponse response = new GetSubmissionConfigResponse();

        response.setClientAppName(fromCacheSubmission.get().getClientAppName());

        response.setNavigationUrls(fromCacheSubmission.get().getNavigationUrls());

        response.setCsoBaseUrl(navigationProperties.getCsoBaseUrl());

        logger.info("Successfully retrieved submission for transactionId [{}]", xTransactionId);

        return ResponseEntity.ok(response);

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<FilingPackage> getSubmissionFilingPackage(UUID xTransactionId, UUID submissionId) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        logger.info("attempting to get Submission Filing Package for transactionId [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        logger.info("successfully retrieved submission filing package for transactionId [{}]", xTransactionId);

        return ResponseEntity.ok(filingPackageMapper.toApiFilingPackage(fromCacheSubmission.get().getFilingPackage()));
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Void> deleteSubmission(UUID submissionId, UUID xTransactionId) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);
        if(!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();
        //Remove documents from cache
        if (fromCacheSubmission.get().getFilingPackage() != null && fromCacheSubmission.get().getFilingPackage().getDocuments() != null)
            fromCacheSubmission.get().getFilingPackage().getDocuments().forEach(
                    document -> documentStore.evict(submissionKey, document.getName()));

        //Remove submission from cache
        submissionStore.evict(submissionKey);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<SubmitResponse> submit(UUID xTransactionId, UUID submissionId, Object body) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);

            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.INVALIDUNIVERSAL).create(),
                    HttpStatus.FORBIDDEN);
        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        logger.info("attempting to submit efiling package for transaction [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();
        ResponseEntity response;
        MDC.put(Keys.MDC_EFILING_SUBMISSION_ID, submissionId.toString());
        try {

            SubmitResponse result = submissionService.createSubmission(fromCacheSubmission.get(), accountService.getCsoAccountDetails(submissionKey.getUniversalId()), SecurityUtils.isEarlyAdopter());

            response = new ResponseEntity(result, HttpStatus.CREATED);
            logger.info("successfully submitted efiling package for transaction [{}], cso id {}", xTransactionId, result.getPackageRef());

        } catch (EfilingSubmissionServiceException e) {
            logger.error("failed package submission {}", xTransactionId);
            response = new ResponseEntity(buildEfilingError(ErrorResponse.SUBMISSION_FAILURE), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (EfilingPaymentException e) {
            logger.error("payment failure during package submission {}", xTransactionId);
            response = new ResponseEntity(buildEfilingError(ErrorResponse.PAYMENT_FAILURE), HttpStatus.BAD_REQUEST);

        } finally {
            this.submissionStore.evict(submissionKey);
        }

        return response;
    }

    public EfilingError buildEfilingError(ErrorResponse errorResponse) {

        EfilingError response = new EfilingError();
        response.setError(errorResponse.getErrorCode());
        response.setMessage(errorResponse.getErrorMessage());
        return response;

    }

    private ResponseEntity storeDocuments(SubmissionKey submissionKey, List<MultipartFile> files) {

        if (files == null || files.isEmpty())
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_REQUIRED).create(),
                    HttpStatus.BAD_REQUEST);

        try {

            for (MultipartFile file : files) {
                try {
                    clamAvService.scan(new ByteArrayInputStream(file.getBytes()));
                } catch (VirusDetectedException e) {
                    return new ResponseEntity(EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_STORAGE_FAILURE).create(),
                            HttpStatus.BAD_GATEWAY);
                }

                if (!TikaAnalysis.isPdf(file))
                    return new ResponseEntity(EfilingErrorBuilder.builder().errorResponse(ErrorResponse.FILE_TYPE_ERROR).create(),
                            HttpStatus.BAD_REQUEST);
            }
            for (MultipartFile file : files) {
                documentStore.put(submissionKey, file.getResource().getFilename(), file.getBytes());
            }

        } catch (IOException e) {
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_STORAGE_FAILURE).create(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("{} stored in cache", files.size());

        return ResponseEntity.ok(new UploadSubmissionDocumentsResponse().submissionId(submissionKey.getSubmissionId()).received(new BigDecimal(files.size())));
    }

}
