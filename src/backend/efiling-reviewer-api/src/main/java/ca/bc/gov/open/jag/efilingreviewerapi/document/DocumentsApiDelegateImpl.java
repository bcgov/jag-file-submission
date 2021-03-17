package ca.bc.gov.open.jag.efilingreviewerapi.document;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.ProjectFieldsResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.Keys;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public DocumentsApiDelegateImpl(
            DiligenService diligenService,
            ExtractRequestMapper extractRequestMapper,
            ExtractStore extractStore,
            StringRedisTemplate stringRedisTemplate,
            FieldProcessor fieldProcessor,
            DocumentValidator documentValidator) {

        this.diligenService = diligenService;
        this.extractRequestMapper = extractRequestMapper;
        this.extractStore = extractStore;
        this.stringRedisTemplate = stringRedisTemplate;
        this.fieldProcessor = fieldProcessor;
        this.documentValidator = documentValidator;

    }

    @Override
    public ResponseEntity<DocumentExtractResponse> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {

        MDC.put(Keys.DOCUMENT_TYPE, xDocumentType);

        long receivedTimeMillis = System.currentTimeMillis();

        logger.info("document extract request received");

        documentValidator.validateDocument(xDocumentType, file);

        logger.info("document is valid");

        ExtractRequest extractRequest = extractRequestMapper.toExtractRequest(xTransactionId, xDocumentType, file, receivedTimeMillis);

        BigDecimal response = diligenService.postDocument(xDocumentType, file);

        //Temporary
        stringRedisTemplate.convertAndSend("documentWait", response.toPlainString());

        Optional<ExtractRequest> extractRequestCached = extractStore.put(response, extractRequest);

        if(!extractRequestCached.isPresent())
            throw new AiReviewerCacheException("Could not cache extract request");

        MDC.remove(Keys.DOCUMENT_TYPE);

        return ResponseEntity.ok(extractRequestMapper.toDocumentExtractResponse(extractRequestCached.get()));

    }

    @Override
    public ResponseEntity<Void> documentEvent(UUID xTransactionId, DocumentEvent documentEvent) {

        logger.info("document {} status has changed to {}", documentEvent.getDocumentId(), documentEvent.getStatus());
        //We won't do anything with this for now
        if (documentEvent.getStatus().equalsIgnoreCase(Keys.PROCESSED_STATUS)) {

            diligenService.getDocumentDetails(documentEvent.getDocumentId());

            ProjectFieldsResponse response = diligenService.getDocumentFieldResponse(documentEvent.getDocumentId());

            Optional<ExtractRequest> extractRequestCached = extractStore.get(documentEvent.getDocumentId());

            if (extractRequestCached.isPresent()) {

                ExtractResponse extractedResponse = ExtractResponse
                        .builder()
                        .document(extractRequestCached.get().getDocument())
                        .formData(buildFormData(response))
                        .extract(extractRequestCached.get().getExtract())
                        .create();

                extractStore.put(documentEvent.getDocumentId(), extractedResponse);

                ExtractRequest extractRequest = extractRequestCached.get();
                MDC.put(Keys.DOCUMENT_TYPE, extractRequest.getDocument().getType());

                extractRequest.updateProcessedTimeMillis();
                logger.info("document processing time: [{}]", extractRequest.getProcessedTimeMillis());
                extractStore.put(documentEvent.getDocumentId(), extractRequest);

            }

        }

        MDC.remove(Keys.DOCUMENT_TYPE);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    private ObjectNode buildFormData(ProjectFieldsResponse response) {

        try {

            File resource = ResourceUtils.getFile("classpath:courtDetails.schema.json");
            return fieldProcessor.getJson(new String(Files.readAllBytes(Paths.get(resource.getPath()))),
                    response.getData().getFields());

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }
}
