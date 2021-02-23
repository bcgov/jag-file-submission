package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public DocumentsApiDelegateImpl(DiligenService diligenService, ExtractRequestMapper extractRequestMapper, ExtractStore extractStore) {
        this.diligenService = diligenService;
        this.extractRequestMapper = extractRequestMapper;
        this.extractStore = extractStore;
    }

    @Override
    public ResponseEntity<DocumentExtractResponse> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {

        logger.info("document extract request received");

        ExtractRequest extractRequest = extractRequestMapper.toExtractRequest(xTransactionId, xDocumentType, file);

        BigDecimal response = diligenService.postDocument(xDocumentType, file);

        Optional<ExtractRequest> extractRequestCached = extractStore.put(response, extractRequest);

        return ResponseEntity.ok(extractRequestMapper.toDocumentExtractResponse(extractRequestCached.get()));

    }
}
