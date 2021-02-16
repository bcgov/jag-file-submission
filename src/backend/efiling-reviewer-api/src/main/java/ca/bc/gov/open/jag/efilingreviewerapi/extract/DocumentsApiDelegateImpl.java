package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {

    Logger logger = LoggerFactory.getLogger(DocumentsApiDelegateImpl.class);

    @Override
    public ResponseEntity<Object> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {

        logger.info("document extract request received");

        return ResponseEntity.ok("Hello");
    }
}
