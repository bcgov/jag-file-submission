package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.diligen.DiligenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Service
public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {

    Logger logger = LoggerFactory.getLogger(DocumentsApiDelegateImpl.class);

    private final DiligenService diligenService;

    public DocumentsApiDelegateImpl(DiligenService diligenService) {
        this.diligenService = diligenService;
    }

    @Override
    public ResponseEntity<Object> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {

        logger.info("document extract request received");

        return ResponseEntity.ok(diligenService.postDocument(xDocumentType, file));

    }
}
