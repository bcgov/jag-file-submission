package ca.bc.gov.open.jag.efilingreviewerapi.document;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenDocumentDetails;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.ProjectFieldsResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.Keys;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerInvalidTransactionIdException;
import ca.bc.gov.open.jag.efilingreviewerapi.webhook.WebHookService;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentConfigException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ProcessedDocumentMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {

    Logger logger = LoggerFactory.getLogger(DocumentsApiDelegateImpl.class);

    private final DiligenService diligenService;
    private final ExtractRequestMapper extractRequestMapper;
    private final ExtractStore extractStore;
    private final StringRedisTemplate stringRedisTemplate;
    private final FieldProcessor fieldProcessor;
    private final DocumentValidator documentValidator;
    private final DocumentTypeConfigurationRepository documentTypeConfigurationRepository;
    private final ProcessedDocumentMapper processedDocumentMapper;
    private final WebHookService webHookService;

    public DocumentsApiDelegateImpl(
            DiligenService diligenService,
            ExtractRequestMapper extractRequestMapper,
            ExtractStore extractStore,
            StringRedisTemplate stringRedisTemplate,
            FieldProcessor fieldProcessor,
            DocumentValidator documentValidator, DocumentTypeConfigurationRepository documentTypeConfigurationRepository, ProcessedDocumentMapper processedDocumentMapper, WebHookService webHookService) {

        this.diligenService = diligenService;
        this.extractRequestMapper = extractRequestMapper;
        this.extractStore = extractStore;
        this.stringRedisTemplate = stringRedisTemplate;
        this.fieldProcessor = fieldProcessor;
        this.documentValidator = documentValidator;
        this.documentTypeConfigurationRepository = documentTypeConfigurationRepository;
        this.processedDocumentMapper = processedDocumentMapper;
        this.webHookService = webHookService;
    }

    @Override
    public ResponseEntity<DocumentExtractResponse> extractDocumentFormData(UUID xTransactionId, String xDocumentType, Boolean xUseWebhook, MultipartFile file) {

        MDC.put(Keys.DOCUMENT_TYPE, xDocumentType);

        DocumentTypeConfiguration documentTypeConfiguration = documentTypeConfigurationRepository.findByDocumentType(xDocumentType);

        if (documentTypeConfiguration == null) {
            return new ResponseEntity("Document type not found", HttpStatus.BAD_REQUEST);
        }

        long receivedTimeMillis = System.currentTimeMillis();

        logger.info("document extract request received");

        documentValidator.validateDocument(xDocumentType, file);

        logger.info("document is valid");

        ExtractRequest extractRequest = extractRequestMapper.toExtractRequest(extractRequestMapper.toExtract(xTransactionId, xUseWebhook), xDocumentType, file, receivedTimeMillis);

        BigDecimal response = diligenService.postDocument(xDocumentType, file, documentTypeConfiguration.getProjectId());

        //Temporary
        stringRedisTemplate.convertAndSend("documentWait", response.toPlainString());

        Optional<ExtractRequest> extractRequestCached = extractStore.put(response, extractRequest);

        if (!extractRequestCached.isPresent())
            throw new AiReviewerCacheException("Could not cache extract request");

        MDC.remove(Keys.DOCUMENT_TYPE);

        return ResponseEntity.ok(extractRequestMapper.toDocumentExtractResponse(extractRequestCached.get(), response));

    }

    @Override
    public ResponseEntity<Void> documentEvent(UUID xTransactionId, DocumentEvent documentEvent) {

        logger.info("document {} status has changed to {}", documentEvent.getDocumentId(), documentEvent.getStatus());

        if (documentEvent.getStatus().equalsIgnoreCase(Keys.PROCESSED_STATUS)) {

            diligenService.getDocumentDetails(documentEvent.getDocumentId());

            DiligenDocumentDetails response = diligenService.getDocumentDetails(documentEvent.getDocumentId());

            Optional<ExtractRequest> extractRequestCached = extractStore.get(documentEvent.getDocumentId());

            extractRequestCached.ifPresent(extractRequest -> {

                DocumentTypeConfiguration config = documentTypeConfigurationRepository.findByDocumentType(extractRequest.getDocument().getType());

                if(config == null)
                    throw new AiReviewerDocumentConfigException("document configuration not found");

                ExtractResponse extractedResponse = ExtractResponse
                        .builder()
                        .document(extractRequestCached.get().getDocument())
                        .formData(buildFormData(response.getProjectFieldsResponse(), config))
                        .extract(extractRequestCached.get().getExtract())
                        .documentValidation(documentValidator.validateExtractedDocument(documentEvent.getDocumentId(), config, response.getAnswers()))
                        .create();

                extractStore.put(documentEvent.getDocumentId(), extractedResponse);

                MDC.put(Keys.DOCUMENT_TYPE, extractRequest.getDocument().getType());

                extractRequest.updateProcessedTimeMillis();
                logger.info("document processing time: [{}]", extractRequest.getProcessedTimeMillis());
                extractStore.put(documentEvent.getDocumentId(), extractRequest);

                if (extractRequestCached.get().getExtract().getUseWebhook()) {
                    //Send document ready message
                    webHookService.sendDocumentReady(documentEvent.getDocumentId(), extractRequest.getDocument().getType());
                }
            });

        }

        MDC.remove(Keys.DOCUMENT_TYPE);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @Override
    public ResponseEntity<ProcessedDocument> documentProcessed(UUID xTransactionId, BigDecimal documentId) {

        logger.info("document {} requested ", documentId);

        Optional<ExtractResponse> extractResponseCached = extractStore.getResponse(documentId);

        if (!extractResponseCached.isPresent()) throw new AiReviewerCacheException("Document not found in cache");
        if (!extractResponseCached.get().getExtract().getTransactionId().equals(xTransactionId)) throw new AiReviewerInvalidTransactionIdException("Requested transaction id is not valid");

        //Clear cache
        extractStore.evict(documentId);
        extractStore.evictResponse(documentId);

        return ResponseEntity.ok(processedDocumentMapper.toProcessedDocument(extractResponseCached.get(), extractResponseCached.get().getDocumentValidation().getValidationResults()));

    }

    private ObjectNode buildFormData(ProjectFieldsResponse response, DocumentTypeConfiguration config) {

        if (config == null)
            throw new AiReviewerDocumentConfigException("Document Configuration not found");

        return fieldProcessor.getJson(config.getDocumentConfig(),
                response.getData().getFields());

    }

}
