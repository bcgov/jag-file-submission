package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.SubmissionApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.*;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.RushProcessingMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import ca.bc.gov.open.jag.efilingapi.utils.TikaAnalysis;
import ca.bc.gov.open.jag.efilingcommons.exceptions.*;
import ca.bc.gov.open.jag.efilingcommons.model.RushProcessing;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableConfigurationProperties(NavigationProperties.class)
public class SubmissionApiDelegateImpl implements SubmissionApiDelegate {

    private static final String ACCOUNT_EXCEPTION = "Client has multiple CSO profiles.";
    private static final String CACHE_ERROR = "Cache related error.";
    private static final String DOCUMENT_REQUIRED = "At least one document is required.";
    private static final String DOCUMENT_STORAGE_FAILURE = "An unknown error happened while storing documents.";
    private static final String DOCUMENT_TYPE_ERROR = "Error while retrieving documents.";
    private static final String FILE_TYPE_EXCEPTION = "File is not a PDF.";
    private static final String INVALID_INITIAL_SUBMISSION_PAYLOAD = "Initial Submission payload invalid, find more in the details array.";
    private static final String INVALID_UNIVERSAL_ID = "Invalid universal id.";
    private static final String INVALID_ROLE = "User does not have a valid role for this request.";
    private static final String MISSING_APPLICATION_CODE = "Missing application code claim. Contact administrator.";
    private static final String MISSING_UNIVERSAL_ID = "universal-id claim missing in jwt token.";
    private static final String PAYMENT_FAILURE = "Error while making payment.";
    private static final String SUBMISSION_FAILURE = "Error while submitting filing package.";
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
    private final RushProcessingMapper rushProcessingMapper;

    public SubmissionApiDelegateImpl(
            SubmissionService submissionService,
            AccountService accountService,
            GenerateUrlResponseMapper generateUrlResponseMapper,
            NavigationProperties navigationProperties,
            SubmissionStore submissionStore, DocumentStore documentStore, ClamAvService clamAvService, FilingPackageMapper filingPackageMapper, GenerateUrlRequestValidator generateUrlRequestValidator, RushProcessingMapper rushProcessingMapper) {
        this.submissionService = submissionService;
        this.accountService = accountService;
        this.generateUrlResponseMapper = generateUrlResponseMapper;
        this.navigationProperties = navigationProperties;
        this.submissionStore = submissionStore;
        this.documentStore = documentStore;
        this.clamAvService = clamAvService;
        this.filingPackageMapper = filingPackageMapper;
        this.generateUrlRequestValidator = generateUrlRequestValidator;
        this.rushProcessingMapper = rushProcessingMapper;
    }

    @Override
    // FIXME: replace with @PreAuthorize
    // @RolesAllowed({"efiling-client", "efiling-admin"})
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadSubmissionDocuments(UUID xTransactionId, String xUserId, List<MultipartFile> files) {

        if(StringUtils.isBlank(xUserId)) {
            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);
        }

        SubmissionKey submissionKey = new SubmissionKey(
                xUserId,
                xTransactionId,
                UUID.randomUUID());

        logger.info("attempting to upload original document [{}]", submissionKey.getSubmissionId());

        ResponseEntity response = storeDocuments(submissionKey, files, false);

        logger.info("successfully uploaded original document [{}]", submissionKey.getSubmissionId());

        return response;

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadAdditionalSubmissionDocuments(UUID submissionId, UUID xTransactionId, List<MultipartFile> files) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        logger.info("attempting to upload new document for transaction [{}]", submissionId);

        ResponseEntity responseEntity = storeDocuments(submissionKey, files, false);

        logger.info("successfully uploaded new document for transaction [{}]", submissionId);

        return responseEntity;
    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<UpdateDocumentResponse> updateDocumentProperties(UUID submissionId, UUID
            xTransactionId, UpdateDocumentRequest updateDocumentRequest) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        logger.info("attempting to add new document for transaction [{}]", submissionId);

        if (updateDocumentRequest == null || updateDocumentRequest.getDocuments().isEmpty())
            throw new DocumentRequiredException(DOCUMENT_REQUIRED);

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        try {

            Submission submission = submissionService.updateDocuments(fromCacheSubmission.get(), updateDocumentRequest, submissionKey);
            UpdateDocumentResponse updateDocumentResponse = new UpdateDocumentResponse();
            SubmissionFilingPackage filingPackage = filingPackageMapper.toApiFilingPackage(submission.getFilingPackage());
            updateDocumentResponse.setDocuments(filingPackage.getDocuments());
            logger.info("successfully added new document for transaction [{}]", submissionId);
            return ResponseEntity.ok(updateDocumentResponse);

        } catch (EfilingDocumentServiceException e) {

            logger.warn(e.getMessage(), e);
            throw new DocumentTypeException(DOCUMENT_TYPE_ERROR);

        }
    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<Resource> getSubmissionDocument(UUID xTransactionId,
                                                          UUID submissionId,
                                                          String filename) {


        logger.info("getSubmission document for transaction [{}]", xTransactionId);

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        byte[] bytes = documentStore.get(submissionKey, filename);

        if (bytes == null) return ResponseEntity.notFound().build();

        logger.info("successfully retrieved document for transaction [{}]", xTransactionId);

        return ResponseEntity.ok(new ByteArrayResource(bytes));

    }

    @Override
    // FIXME: replace with @PreAuthorize
    // @RolesAllowed({"efiling-client", "efiling-admin"})
    public ResponseEntity<GenerateUrlResponse> generateUrl(UUID xTransactionId, String xUserId, UUID submissionId, GenerateUrlRequest generateUrlRequest) {

        logger.info("Attempting to generate Url Request Received");

        logger.info("Message recieved from client App [{}]", generateUrlRequest.getClientAppName());

        if (StringUtils.isBlank(xUserId)) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        Optional<String> applicationCode = SecurityUtils.getApplicationCode();

        if (!applicationCode.isPresent()) {

            logger.error("[{}]: {}", ErrorCode.MISSING_APPLICATION_CODE.toString(), MISSING_APPLICATION_CODE);
            throw new MissingApplicationCodeException(MISSING_APPLICATION_CODE);

        }

        Notification validation = generateUrlRequestValidator.validate(generateUrlRequest, applicationCode.get(), xUserId);

        if (validation.hasError())
            throw new InvalidInitialSubmissionPayloadException(INVALID_INITIAL_SUBMISSION_PAYLOAD, validation.getErrors());

        if (accountService.getCsoAccountDetails(xUserId) != null &&
                !accountService.getCsoAccountDetails(xUserId).isFileRolePresent())
            throw new InvalidRoleException(INVALID_ROLE);

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
            throw new AccountException(ACCOUNT_EXCEPTION);
        } catch (InvalidAccountStateException e) {
            logger.warn(e.getMessage(), e);
            throw new InvalidRoleException(INVALID_ROLE);
        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            throw new DocumentTypeException(DOCUMENT_TYPE_ERROR);
        } catch (StoreException e) {
            logger.warn(e.getMessage(), e);
            throw new CacheException(CACHE_ERROR);
        }

        return response;

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<GetSubmissionConfigResponse> getSubmissionConfig(UUID submissionId, UUID xTransactionId) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if (!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new MissingUniversalIdException(MISSING_UNIVERSAL_ID);

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
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<SubmissionFilingPackage> getSubmissionFilingPackage(UUID xTransactionId, UUID submissionId) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        logger.info("attempting to get Submission Filing Package for transactionId [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        logger.info("successfully retrieved submission filing package for transactionId [{}]", xTransactionId);

        SubmissionFilingPackage submissionFilingPackage = filingPackageMapper.toApiFilingPackage(fromCacheSubmission.get().getFilingPackage());

        submissionFilingPackage.getDocuments().forEach(
                submissionDocument -> submissionDocument.setRushRequired(submissionService.isRushRequired(submissionDocument.getType(),submissionFilingPackage.getCourt().getLevel(),submissionFilingPackage.getCourt().getCourtClass()))
        );

        return ResponseEntity.ok(submissionFilingPackage);

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<Void> deleteSubmission(UUID submissionId, UUID xTransactionId) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

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
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<SubmitResponse> submit(UUID xTransactionId, UUID submissionId, Object body) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

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
            throw new SubmissionException(SUBMISSION_FAILURE);

        } catch (EfilingPaymentException e) {
            logger.error("payment failure during package submission {}", xTransactionId);
            throw new PaymentException(PAYMENT_FAILURE);

        } finally {
            this.submissionStore.evict(submissionKey);
        }

        return response;
    }

    private ResponseEntity storeDocuments(SubmissionKey submissionKey, List<MultipartFile> files, boolean rush) {

        if (files == null || files.isEmpty())
            throw new DocumentRequiredException(DOCUMENT_REQUIRED);

        try {

            for (MultipartFile file : files) {
                try {
                    clamAvService.scan(new ByteArrayInputStream(file.getBytes()));
                } catch (VirusDetectedException e) {
                    throw new DocumentStorageException(DOCUMENT_STORAGE_FAILURE);
                }

                if (!TikaAnalysis.isPdf(file))
                    throw new FileTypeException(FILE_TYPE_EXCEPTION);
            }
            for (MultipartFile file : files) {
                if (!rush) {
                    documentStore.put(submissionKey, file.getResource().getFilename(), file.getBytes());
                } else {
                    documentStore.putRushDocument(submissionKey, file.getResource().getFilename(), file.getBytes());
                }
            }

        } catch (IOException e) {
            throw new DocumentStorageException(DOCUMENT_STORAGE_FAILURE);
        }

        logger.info("{} stored in cache", files.size());

        return ResponseEntity.ok(new UploadSubmissionDocumentsResponse().submissionId(submissionKey.getSubmissionId()).received(new BigDecimal(files.size())));

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<Void> postRushProcessing(UUID xTransactionId, UUID submissionId, Rush rush) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        SubmissionKey submissionKey = new SubmissionKey(universalId.get(), xTransactionId, submissionId);

        logger.info("attempting to add rush processing to efiling package for transaction [{}]", xTransactionId);

        Optional<Submission> fromCacheSubmission = this.submissionStore.get(submissionKey);

        if (!fromCacheSubmission.isPresent())
            return ResponseEntity.notFound().build();

        MDC.put(Keys.MDC_EFILING_SUBMISSION_ID, submissionId.toString());

        RushProcessing rushProcessing = rushProcessingMapper.toRushProcessing(rush);
        //Set filenames here values not present in mapper
        rushProcessing.getSupportingDocuments().forEach(
            document -> document.setServerFileName(MessageFormat.format("fh_{0}_{1}_{2}", submissionKey.getSubmissionId(), submissionKey.getTransactionId(), document.getName())
        ));

        fromCacheSubmission.get().getFilingPackage().setRush(rushProcessing);

        submissionStore.put(fromCacheSubmission.get());

        return ResponseEntity.created(null).build();

    }

    @Override
    @PreAuthorize("hasRole('efiling-user')")
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadRushDocuments(UUID submissionId, UUID xTransactionId, List<MultipartFile> files) {

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) {

            logger.error(UNIVERSAL_ID_IS_REQUIRED);
            throw new InvalidUniversalException(INVALID_UNIVERSAL_ID);

        }

        SubmissionKey submissionKey = new SubmissionKey(
                universalId.get(),
                xTransactionId,
                submissionId);

        logger.info("attempting to upload rush document [{}]", submissionKey.getSubmissionId());

        ResponseEntity response = storeDocuments(submissionKey, files, true);

        logger.info("successfully uploaded rush document [{}]", submissionKey.getSubmissionId());

        return response;
        
    }
}
