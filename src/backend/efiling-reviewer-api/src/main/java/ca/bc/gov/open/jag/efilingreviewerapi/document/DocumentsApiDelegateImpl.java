package ca.bc.gov.open.jag.efilingreviewerapi.document;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentEvent;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerVirusFoundException;
import ca.bc.gov.open.jag.efilingreviewerapi.utils.TikaAnalysis;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
@Service
public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {

    Logger logger = LoggerFactory.getLogger(DocumentsApiDelegateImpl.class);

    private final DiligenService diligenService;
    private final ExtractRequestMapper extractRequestMapper;
    private final ExtractStore extractStore;


    private final ClamAvService clamAvService;

    public DocumentsApiDelegateImpl(DiligenService diligenService, ExtractRequestMapper extractRequestMapper, ExtractStore extractStore, ClamAvService clamAvService) {
        this.diligenService = diligenService;
        this.extractRequestMapper = extractRequestMapper;
        this.extractStore = extractStore;
        this.clamAvService = clamAvService;
    }

    @Override
    public ResponseEntity<DocumentExtractResponse> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {

        logger.info("document extract request received");

        try {
            clamAvService.scan(new ByteArrayInputStream(file.getBytes()));
            if (!TikaAnalysis.isPdf(file)) throw new AiReviewerDocumentException("Invalid file type");
        } catch (VirusDetectedException e) {
            throw new AiReviewerVirusFoundException("Virus found in document");
        } catch (IOException e) {
            throw new AiReviewerDocumentException("File is corrupt");
        }

        ExtractRequest extractRequest = extractRequestMapper.toExtractRequest(xTransactionId, xDocumentType, file);

        BigDecimal response = diligenService.postDocument(xDocumentType, file);

        Optional<ExtractRequest> extractRequestCached = extractStore.put(response, extractRequest);

        if(!extractRequestCached.isPresent())
            throw new AiReviewerCacheException("Could not cache extract request");

        return ResponseEntity.ok(extractRequestMapper.toDocumentExtractResponse(extractRequestCached.get()));

    }

    @Override
    public ResponseEntity<Void> documentEvent(UUID xTransactionId, DocumentEvent documentEvent) {

        logger.info("document {} status has changed to {}", documentEvent.getDocumentId(), documentEvent.getStatus());
        //We won't do anything with this for now
        Object result = diligenService.getDocumentDetails(documentEvent.getDocumentId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
